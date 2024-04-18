package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GenerateTheQuizDTO;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.memory.SimpleMemory;
import de.htwg.multipleChoice.repositories.StudentRepository;
import de.htwg.multipleChoice.services.*;
import de.htwg.multipleChoice.tools.RequestType;
import de.htwg.rag.ingestor.DocumentIngestor;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * TODO: Add RAG to normal chat with user input text and metadata with "#:~:text="
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
    UserInputClassifierAIService userInputClassifier;
    @Inject
    WebScraperService webScraperService;
    @Inject
    PersonalizedStudentChatAIService chatService;
    @Inject
    StudentRepository studentRepository;
    @Inject
    DocumentIngestor documentIngestor;

    /**
     * This method implements a chain of AIServices to generate a quiz.
     * It first classifies the user input, then decides how to get to the data or
     * just to answer the question.
     * After that if it decides the user wants a quiz and
     * calls an AIService that has tools to generate a quiz.
     *
     * @param  userInput      the user message with the scriptId or scriptName
     * @param  conversationId the id of the conversation
     * @return                either a quizId (UUID as a String) or an error message
     */
    public String startTheChain(String userInput, UUID conversationId, String studentId) {

        // Determine if the user input is an url or a text and get the data
        String data = "";
        // List<RequestType> type = userInputClassifier.classifyInput(userInput);
        RequestType type = userInputClassifier.classify(userInput);
        // no data was provided
         if(type == RequestType.NO_DATA){

             // add the username to the context to make it more personal
             Student student = studentRepository.findById(UUID.fromString(studentId));
             userInput += "\n\n\n context: {username: " + student.getName() + "}";

             return chatService.chat(conversationId, userInput, studentId);
         }
        // text input
         if(type == RequestType.TEXT){
             data = userInput;
             Document document = Document.document(data);
             document.metadata().add("studentId", studentId);
             documentIngestor.ingest(List.of(document));
         }
        // url
         if(type == RequestType.URL){

             data = webScraperService.scrapeURL(userInput, conversationId);
             Document document = Document.document(data);
             document.metadata().add("studentId", studentId);
             documentIngestor.ingest(List.of(document));

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

        // Second step is to generate a new quiz and then add the questions to the quiz.
        GenerateTheQuizDTO generateTheQuizDTO = generateTheQuizAIService.generateTheQuiz(data, conversationId);

        // After that it should be sent to the user
        if (generateTheQuizDTO.getSuccess()) {
            return generateTheQuizDTO.getQuizId();
        }
        else {
            return generateTheQuizDTO.getMessage();
        }
    }
}
