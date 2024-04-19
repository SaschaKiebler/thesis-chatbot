package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.LectureResultDTO;
import de.htwg.multipleChoice.DTOs.PostQuizResultsDTO;
import de.htwg.multipleChoice.DTOs.QuizChainInputDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.entities.PossibleAnswer;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.memory.SimpleMemory;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.PossibleAnswerRepository;
import de.htwg.multipleChoice.repositories.StudentRepository;
import de.htwg.multipleChoice.services.PossibleFollowUpQuestionsAIService;
import de.htwg.multipleChoice.services.QuizResultAIService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to get the quiz, add the results to the conversation and talk with the ai
 * about the completed quiz
 */
@Path("/api/quiz")
@ApplicationScoped
public class MultipleChoiceAPIResource {

    @Inject
    ConversationRepository conversationRepository;
    @Inject
    MCQuizRepository mcQuizRepository;
    @Inject
    PossibleAnswerRepository possibleAnswerRepository;
    SimpleMemory memory = new SimpleMemory();
    @Inject
    QuizResultAIService quizResultAIService;
    @Inject
    PossibleFollowUpQuestionsAIService possibleFollowUpQuestionsAIService;
    @Inject
    LectureRepository lectureRepository;
    @Inject
    StudentRepository studentRepository;

    /**
     * Method to get a specific quiz and return it to the user in JSON
     * @param quizId the id of the quiz
     * @param conversationId the id of the conversation
     * @return the quiz
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuiz(@QueryParam("quizId") String quizId, @QueryParam("conversationId") String conversationId) {
        MCQuiz quiz = mcQuizRepository.findById(UUID.fromString(quizId));
        System.out.println("Get quiz with id: " + quizId);


        if (quiz == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(Json.object().put("quiz", quiz.toString()).build()).build();
    }

    /**
     * Method to add the quiz result to the conversation so that the ai knows what the user answered
     * @param quizResults
     * @return code 200 if successful
     */
    @POST
    @Path("result")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response setQuizResult(@RequestBody PostQuizResultsDTO quizResults) {
        // get the conversation and the quiz
        MCQuiz quiz;
        Conversation conversation;
        try {
            quiz = mcQuizRepository.findById(UUID.fromString(quizResults.getQuizId()));
            conversation = conversationRepository.findById(UUID.fromString(quizResults.getConversationId()));
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int correctAnswers = 0;
        int totalAnswers = 0;

        // add the quiz and the results to the conversation
        StringBuilder userAnswers = new StringBuilder();
        userAnswers.append("Meine Antworten: ");
        for (String answer : quizResults.getResults()) {
            PossibleAnswer possibleAnswer = possibleAnswerRepository.findById(UUID.fromString(answer));
            userAnswers.append("\n").append(possibleAnswer.getAnswer());
            if (possibleAnswer.getCorrect()) {
                correctAnswers++;
            }
            totalAnswers++;
        }
        memory.updateMessages(conversation.getId(), List.of(
                new AiMessage("Hier ist dein Quiz: " + quiz.toString())));
        memory.updateMessages(conversation.getId(), List.of(
                new UserMessage(userAnswers.toString())
        ));
        memory.updateMessages(conversation.getId(), List.of(
                new AiMessage("Was möchtest du nochmal genauer besprechen?")
        ));

        // calculate the result and add it to the quiz
        float result =  (float) correctAnswers / totalAnswers;
        quiz.setResult(result);
        mcQuizRepository.persist(quiz);

        // get the possible follow-up questions and put them in a json array
        List<String> possibleFollowUpQuestions = possibleFollowUpQuestionsAIService.possibleQuestionsChat(memory.getMessages(conversation.getId()).toString());
        Json.JsonArrayBuilder possibleFollowUpQuestionsJson = Json.array();
        for (String question : possibleFollowUpQuestions) {
            possibleFollowUpQuestionsJson.add(question);
        }
        // return the possible follow-up questions
        return Response.ok().entity(Json.object().put("possibleFollowUpQuestions", possibleFollowUpQuestionsJson).build()).build();
    }

    @Path("results/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentResults(@PathParam("studentId") String studentId) {
        try {
            Student student = studentRepository.findById(UUID.fromString(studentId));
            List<MCQuiz> studentResults = mcQuizRepository.findAllFromStudent(student.getId());
            List<Lecture> studentLectures = student.getLectures();
            Map<String, List<Float>> scoresByLecture = new HashMap<>();

            for (Lecture lecture : studentLectures) {
                scoresByLecture.put(lecture.getName(), new ArrayList<>());
            }

            for (MCQuiz result : studentResults) {
                List<Float> scores = scoresByLecture.get(result.getLecture().getName());
                if (scores != null) {
                    scores.add(result.getResult());
                }
            }

            Map<String, Float> averageResultsByLecture = new HashMap<>();
            for (Map.Entry<String, List<Float>> entry : scoresByLecture.entrySet()) {
                List<Float> scores = entry.getValue();
                float average = 0;
                if (!scores.isEmpty()) {
                    float sum = 0;
                    for (Float score : scores) {
                        sum += score;
                    }
                    average = sum / scores.size();
                }
                averageResultsByLecture.put(entry.getKey(), average);
            }

            LectureResultDTO lectureResultDTO = new LectureResultDTO(averageResultsByLecture);
            System.out.println("Get student results: " + lectureResultDTO);
            return Response.ok().entity(lectureResultDTO).build();
        } catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }




    // not used at the moment
    /**
     * Method to talk with the ai after completing the quiz. will provide possible follow up questions
     * @param userInput a object of type QuizChainInputDTO with the user-message and the conversationId
     * @return answer and possible follow up questions
     */
    @POST
    @Path("talk")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getAiResponse(@RequestBody QuizChainInputDTO userInput) {
        String aiResponse = quizResultAIService.resultChat(userInput.getMessage(), UUID.fromString(userInput.getConversationId()));
        List<String> possibleFollowUpQuestions = possibleFollowUpQuestionsAIService.possibleQuestionsChat(aiResponse);
        Json.JsonArrayBuilder possibleFollowUpQuestionsJson = Json.array();
        for (String question : possibleFollowUpQuestions) {
            possibleFollowUpQuestionsJson.add(question);
        }
        return Response.ok().entity(Json.object().put("answer", aiResponse).put("possibleFollowUpQuestions", possibleFollowUpQuestionsJson).build()).build();
    }
}
