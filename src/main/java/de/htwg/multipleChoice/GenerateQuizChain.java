package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.LectureDTO;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GenerateTheQuizDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.memory.SimpleMemory;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.StudentRepository;
import de.htwg.multipleChoice.services.*;
import de.htwg.multipleChoice.enums.RequestType;
import de.htwg.rag.ingestor.DocumentIngestor;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class implements a chain of AIServices to generate a quiz or just handle the conversation.
 */
@ApplicationScoped
public class GenerateQuizChain {

    // This class will chain multiple AIServices with specifc tools together and make
    // the quiz workflow more robust. Also it helps adding stuff like a router between
    // the Experts. The Advantage of this approach is that it will be easier to add
    // different models with different strengths to the conversation and not distract the
    // models from doing one thing right.
    @Inject
    GenerateTheQuizAIService generateTheQuizAIService;
    @Inject
    InputClassifierAIService inputClassifier;
    @Inject
    WebScraperService webScraperService;
    @Inject
    PersonalizedStudentChatAIService chatService;
    @Inject
    StudentRepository studentRepository;
    @Inject
    DocumentIngestor documentIngestor;
    @Inject
    LectureClassifierAIService lectureClassifierAIService;
    @Inject
    MCQuizRepository mcQuizRepository;
    @Inject
    ConversationRepository conversationRepository;

    /**
     * This method implements a chain of AIServices to generate a quiz.
     * It first classifies the user input, then decides how to get to the data or
     * just to answer the question.
     * After that if it decides the user wants a quiz and
     * calls an AIService that has tools to generate a quiz.
     * It also classifies the input text to determine which lecture it corresponds to, along with the associated quiz.
     *
     * @param  userInput      the user message with the scriptId or scriptName
     * @param  conversationId the id of the conversation
     * @return                either a quizId (UUID as a String) or an error message
     */
    @Transactional
    public String startTheChain(String userInput, UUID conversationId, String studentId) {

        // get the student
        Student student = studentRepository.findById(UUID.fromString(studentId));

        // Determine if the user input is an url or a text and get the data
        String data = "";
        String lectureName = "";
        // List<RequestType> type = userInputClassifier.classifyInput(userInput);
        RequestType type = inputClassifier.classify(userInput);
        // no data was provided
         if(type == RequestType.NO_DATA){

             // add the username to the context to make it more personal
             userInput += "\n\n\n context: {username: " + student.getName() + "}";

             return chatService.chat(conversationId, userInput, studentId);
         }
        // text input
         if(type == RequestType.TEXT){
             data = userInput;
             // get the Students lectures he currently takes
             List<String> lectureNames = getTheLectureNames(student);
             // classify the text
             try {
                 lectureName = classifyDataForLecture(data, lectureNames);
             }catch (Exception e){
                 e.printStackTrace();
                 lectureName = "";
             }
         }
        // url
         if(type == RequestType.URL){
             // get the Students lectures he currently takes
             List<String> lectureNames = getTheLectureNames(student);
             // scrape the text from the url
             data = webScraperService.scrapeURL(userInput, conversationId);
             // classify the text
             try {
                 lectureName = classifyDataForLecture(data, lectureNames);
             }catch (Exception e){
                 e.printStackTrace();
                 lectureName = "";
             }
             // ingest the data into the rag
             ingestDataIntoRAG(data, studentId, lectureName);

         }
         // create another quiz
         if(type == RequestType.SAME_TEXT){
             //needs to get fixed bc now it takes all user messages and not just the user data input
             //TODO: add a method that stores the data given from the user to the current conversation
             SimpleMemory memory = new SimpleMemory();
             List<ChatMessage> messages = memory.getMessages(conversationId);
             StringBuilder inputText = new StringBuilder();
             for (ChatMessage message : messages) {
                 if (message instanceof UserMessage) {
                     inputText.append(((UserMessage) message).contents().toString());
                 }
             }
             inputText.append("\n").append(userInput);
             data = inputText.toString();
         }

         // TODO: add methods that connect the data from pubmed etc. to the user input


        // calculate the current userScore in the lecture and set the difficulty accordingly
        String difficulty = "hard";
        Lecture currentLecture = getTheLecture(lectureName, student.getLectures());
        ArrayList <Float> scoreList = new ArrayList<>();

        if(currentLecture != null) {
            List<MCQuiz> studentQuizzes = mcQuizRepository.findAllFromStudent(student.getId());
            if(studentQuizzes != null) {
                studentQuizzes.stream().filter(
                        quiz -> quiz.getLecture().getId().equals(currentLecture.getId())
                ).forEach(quiz -> scoreList.add(quiz.getResult()));
            }
            float avaerage = 0;
            if(!scoreList.isEmpty()) {
                float sum = 0;
                for (Float aFloat : scoreList) {
                    sum += aFloat;
                }
                avaerage = sum / scoreList.size();
            }
            if (avaerage == 0f) {
                difficulty = "easy";
            } else if (avaerage <= 0.5f) {
                difficulty = "medium";
            } else if (avaerage > 0.5f) {
                difficulty = "hard";
            }
        }

        // Second step is to generate a new quiz and then add the questions to the quiz.
        GenerateTheQuizDTO generateTheQuizDTO = generateTheQuizAIService.generateTheQuiz(data, difficulty, conversationId);

        // After that it should be sent to the user
        if (generateTheQuizDTO.getSuccess()) {
            // add quiz to the conversation and the student
            MCQuiz mcQuiz = null;
            try {
                Conversation conversation = conversationRepository.findById(conversationId);
                mcQuiz = mcQuizRepository.findById(UUID.fromString(generateTheQuizDTO.getQuizId()));
                mcQuiz.setConversation(conversation);
                mcQuiz.setStudent(student);
                mcQuizRepository.persist(mcQuiz);
            }catch (Exception e) {
                e.printStackTrace();
            }

            // add the classified lecture to the quiz
            Lecture lecture = student.getLectures() != null ? getTheLecture(lectureName, student.getLectures()) : null;
            if (lecture != null && mcQuiz != null) {
                try {
                    mcQuiz.setLecture(lecture);
                    mcQuizRepository.persist(mcQuiz);
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // return the generated quizId
            return generateTheQuizDTO.getQuizId();
        }
        else {
            return generateTheQuizDTO.getMessage();
        }
    }

    private Lecture getTheLecture(String lectureName, List<Lecture> lectures) {
        for(Lecture lecture : lectures) {
            if(lecture.getName().equals(lectureName)) {
                return lecture;
            }
        }
        return null;
    }

    private List<String> getTheLectureNames(Student student) {
        List<Lecture> lectures = student.getLectures();
        List<String> lectureNames = List.of("");
        if (lectures != null) {
            lectureNames = lectures.stream().map(Lecture::getName).toList();
        }
        return lectureNames;
    }

    private String classifyDataForLecture(String data, List<String> lectureNames) {
        LectureDTO lectureDTO = lectureClassifierAIService.classifyInput(data, lectureNames);
        return lectureDTO.getName() != null ? lectureDTO.getName() : "";
    }

    private void ingestDataIntoRAG(String data, String studentId, String lectureName) {
        Document document = Document.document(data);
        document.metadata().add("studentId", studentId);
        document.metadata().add("lectureName", lectureName);
        documentIngestor.ingest(List.of(document));
    }

}
