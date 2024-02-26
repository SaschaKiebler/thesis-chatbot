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

    @ConfigProperty(name = "ai.left-service", defaultValue = "openai")
    String leftServiceName;
    @ConfigProperty(name = "ai.right-service", defaultValue = "openai")
    String rightServiceName;
    @ConfigProperty(name = "ai.left-service.rag", defaultValue = "true")
    Boolean leftServiceRag;
    @ConfigProperty(name = "ai.right-service.rag", defaultValue = "false")
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
            return Json.object().put("error", "bitte gebe eine Nachricht ein").build();
        }

        Conversation conversation = getConversation(conversationId,side);
        String answer = getAnswer(message, conversation.getId().toString(), side);

        if (answer == null || answer.isEmpty()) {
            return Json.object().put("error", "etwas ist mit der KI schiefgelaufen versuche es später nochmal...").build();
        }

        Message messageFromDb = messageRepository.findLatestMessageFromConversation(conversation.getId());
        if (messageFromDb == null) {
            return Json.object().put("error", "etwas ist mit der DB schiefgelaufen versuche es später nochmal...").build();
        }

        Answer savedAnswer = answerRepository.findByMessageId(messageFromDb.getId());
        if (savedAnswer == null) {
            return Json.object().put("error", "etwas ist mit der Antwort-ID schiefgelaufen versuche es später nochmal..." + messageFromDb.getId()).build();
        }

        return Json.object().put("answer", answer)
                .put("conversationId", conversation.getId().toString())
                .put("answerId", savedAnswer.getId().toString())
                .build();
    }

    private Conversation getConversation(String conversationId, String side) {
        if (conversationId != null && !conversationId.isEmpty()) {
            return conversationRepository.findById(UUID.fromString(conversationId));
        } else {
            Conversation conversation = new Conversation();
            conversation.setRag((side.equals("left") && leftServiceRag) || (!side.equals("left") && rightServiceRag));
            conversationRepository.persist(conversation);
            return conversation;
        }
    }

    private String getAnswer(String message, String conversationId, String side) {
        if (!side.equals("left") && !side.equals("right")) {
            return null;
        }

        boolean isLeftSide = side.equals("left");
        String serviceName = isLeftSide ? leftServiceName : rightServiceName;
        boolean serviceRag = isLeftSide ? leftServiceRag : rightServiceRag;

        return switch (serviceName) {
            case "togetherai" -> serviceRag ? togetherAIService.chat(conversationId, message)
                    : togetherAIServiceNoRAG.chat(conversationId, message);
            case "openai" -> serviceRag ? openAIService.chat(conversationId, message)
                    : openAIServiceNoRAG.chat(conversationId, message);
            default -> null;
        };
    }


}
