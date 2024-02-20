package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import io.quarkiverse.langchain4j.ModelName;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/llm")
@ApplicationScoped
public class LLMResource {

    @Inject
    OpenAIService openAIService;

    @Inject
    TogetherAIService togetherAIService;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    MessageRepository messageRepository;

    @POST
    @Path("/commercial")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sendRequestCommercial(@QueryParam("message") String message, @QueryParam("conversationId") String conversationId) {
        // test for null or empty input
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }

        // Set the conversation to provide a memory id for the chat
        Conversation conversation = getConversation(conversationId);

        // get the answer from the AI
        String answer = openAIService.chat(conversation.getId().toString(), message);

        if (answer == null) {
            return "Sorry, the service is currently not available. Please try again later.";
        }
        // get answerId from the db
        Message messageFromDb = messageRepository.findByConversationIdAndMessage(conversation.getId(), message);
        Answer safedAnswer = answerRepository.findByMessageIdAndAnswerText(messageFromDb.getId(), answer);

        if (safedAnswer == null) {
            return "Something went wrong. Please try again later";
        }

        return Json.object().put("answer",answer).put("conversationId", conversation.getId().toString()).put("answerId",safedAnswer.getId().toString()).build();

    }

    @POST
    @Path("/opensource")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sendRequestOpenSource(@QueryParam("message") String message, @QueryParam("conversationId") String conversationId) {
        // test for null or empty input
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }

        // Set the conversation to provide a memory id for the chat
        Conversation conversation = getConversation(conversationId);

        // get the answer from the AI
        String answer = togetherAIService.chat(conversation.getId().toString(), message);

        if (answer == null) {
            return "Sorry, the service is currently not available. Please try again later.";
        }
        return Json.object().put("answer",answer).put("conversationId", conversation.getId().toString()).build();
    }

    private Conversation getConversation(String conversationId) {
        if (conversationId != null && !conversationId.isEmpty()) {
            return conversationRepository.findById(UUID.fromString(conversationId));
        } else {
            Conversation conversation = new Conversation();
            conversationRepository.persist(conversation);
            return conversation;
        }
    }

}
