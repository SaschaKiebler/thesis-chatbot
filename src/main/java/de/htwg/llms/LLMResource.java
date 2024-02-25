package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.llms.services.OpenAIService;
import de.htwg.llms.services.OpenAIServiceNoRAG;
import de.htwg.llms.services.TogetherAIService;
import de.htwg.llms.services.TogetherAIServiceNoRAG;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.UUID;

@Path("/llm")
@ApplicationScoped
public class LLMResource {

    @ConfigProperty(name = "ai.left-service")
    String leftServiceName;

    @ConfigProperty(name = "ai.right-service")
    String rightServiceName;

    @ConfigProperty(name = "ai.left-service.rag")
    Boolean leftServiceRag;

    @ConfigProperty(name = "ai.right-service.rag")
    Boolean rightServiceRag;

    @Inject
    TogetherAIServiceNoRAG togetherAIServiceNoRAG;

    @Inject
    TogetherAIService togetherAIService;

    @Inject
    OpenAIServiceNoRAG openAIServiceNoRAG;

    @Inject
    OpenAIService openAIService;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    MessageRepository messageRepository;

    @POST
    @Path("/{side}Service")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sendRequestService(@QueryParam("message") String message, @QueryParam("conversationId") String conversationId, @PathParam("side") String side) {
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }

        Conversation conversation = getConversation(conversationId);
        String answer = getAnswer(message, conversation, side);

        if (answer == null) {
            return "Sorry, the service is currently not available. Please try again later.";
        }

        Message messageFromDb = messageRepository.findByConversationIdAndMessage(conversation.getId(), message);
        if (messageFromDb == null) {
            return "Something went wrong. No message found. Please try again later";
        }

        Answer savedAnswer = answerRepository.findByMessageIdAndAnswerText(messageFromDb.getId(), answer);
        if (savedAnswer == null) {
            return "Something went wrong. No Answer found. Please try again later";
        }

        return Json.object().put("answer", answer)
                .put("conversationId", conversation.getId().toString())
                .put("answerId", savedAnswer.getId().toString())
                .build();
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

    private String getAnswer(String message, Conversation conversation, String side) {
        if (!side.equals("left") && !side.equals("right")) {
            return null;
        }

        boolean isLeftSide = side.equals("left");
        String serviceName = isLeftSide ? leftServiceName : rightServiceName;
        boolean serviceRag = isLeftSide ? leftServiceRag : rightServiceRag;

        String conversationId = conversation.getId().toString();
        switch (serviceName) {
            case "togetherai":
                return serviceRag ? togetherAIService.chat(conversationId, message)
                        : togetherAIServiceNoRAG.chat(conversationId, message);
            case "openai":
                return serviceRag ? openAIService.chat(conversationId, message)
                        : openAIServiceNoRAG.chat(conversationId, message);
            default:
                return null;
        }
    }


}
