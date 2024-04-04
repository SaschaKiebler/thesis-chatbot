package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.PostQuizResultsDTO;
import de.htwg.multipleChoice.DTOs.QuizChainInputDTO;
import de.htwg.multipleChoice.entities.*;
import de.htwg.multipleChoice.memory.SimpleMemory;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.PossibleAnswerRepository;
import de.htwg.multipleChoice.services.PossibleFollowUpQuestionsAIService;
import de.htwg.multipleChoice.services.QuizResultAIService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import io.quarkus.vertx.http.runtime.devmode.Json;
import io.vertx.core.json.JsonArray;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public Response getQuizResult(@RequestBody PostQuizResultsDTO quizResults) {
        List<PossibleAnswer> answers = new ArrayList<>();
        MCQuiz quiz = mcQuizRepository.findById(UUID.fromString(quizResults.getQuizId()));
        Conversation conversation = conversationRepository.findById(UUID.fromString(quizResults.getConversationId()));
        StringBuilder userAnswers = new StringBuilder();
        userAnswers.append("Meine Antworten: ");
        for (String answer : quizResults.getResults()) {
            PossibleAnswer possibleAnswer = possibleAnswerRepository.findById(UUID.fromString(answer));
            answers.add(possibleAnswer);
            userAnswers.append("\n").append(possibleAnswer.getAnswer());
        }
        System.out.println(userAnswers.toString());
        memory.updateMessages(conversation.getId(), List.of(
                new AiMessage("Hier ist dein Quiz: " + quiz.toString())));
        memory.updateMessages(conversation.getId(), List.of(
                new UserMessage(userAnswers.toString())
        ));
        memory.updateMessages(conversation.getId(), List.of(
                new AiMessage("Was möchtest du nochmal genauer besprechen?")
        ));

        return Response.ok().build();
    }

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
