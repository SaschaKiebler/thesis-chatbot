package de.htwg.chat.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.llms.services.OpenAIService;
import de.htwg.chat.llms.services.OpenAIServiceNoRAG;
import de.htwg.chat.llms.services.TogetherAIService;
import de.htwg.chat.llms.services.TogetherAIServiceNoRAG;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

/**
 * This class is the Resource for the LLM.
 * It serves the LLM-Answers for the Chat-UI.
 * It can be configured with the variables ai.left-service, ai.right-service, ai.left-service.rag, ai.right-service.rag and ai.prompt.
 *
 */
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

    @ConfigProperty(name = "ai.prompt")
    String prompt;

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

    /**
     * This method is called when a POST request is sent to /llm/{side}Service.
     * It sends a message to the LLM and returns the answer.
     * @param message The message to send to the LLM.
     * @param conversationId The id of the conversation.
     * @param side The configuration-side from the application.properties.
     * @return The answer from the LLM.
     */
    @POST
    @Path("/{side}Service")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sendRequestService(@RequestBody(ref = "message") String message, @QueryParam("conversationId") String conversationId, @PathParam("side") String side) {
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

        // add the model and rag status to the answer
        String modelAndRag;
        if (side.equals("left")) {
            modelAndRag = leftServiceName + " " + (leftServiceRag ? " with RAG" : "without RAG");
        }else {
            modelAndRag = rightServiceName + " " + (rightServiceRag ? " with RAG" : "without RAG");
        }


        return Json.object().put("answer", answer)
                .put("conversationId", conversation.getId().toString())
                .put("answerId", savedAnswer.getId().toString())
                .put("modelname", modelAndRag)
                .build();
    }

    /**
     * This method gets the conversation from the database or creates a new one.
     * @param conversationId The id of the conversation.
     *                       If it is null or empty a new conversation is created.
     * @param side The configuration-side from the application.properties.
     *             It is used to set the service-name and the RAG.
     */
    private Conversation getConversation(String conversationId, String side) {
        if (conversationId != null && !conversationId.isEmpty()) {
            return conversationRepository.findById(UUID.fromString(conversationId));
        } else {
            Conversation conversation = new Conversation();
            conversation.setRag((side.equals("left") && leftServiceRag) || (!side.equals("left") && rightServiceRag));
            if (side.equals("left")) {
                conversation.setServiceName(leftServiceName);
            } else {
                conversation.setServiceName(rightServiceName);
            }
            conversationRepository.persist(conversation);
            return conversation;
        }
    }

    /**
     * This method gets the answer from the LLM and chooses the Service that was configured.
     * @param message The message to send to the LLM.
     * @param conversationId The id of the conversation.
     * @param side The configuration-side from the application.properties.
     *             It is used to set the service-name and the RAG.
     * @return The answer from the LLM.
     */
    private String getAnswer(String message, String conversationId, String side) {
        if (!side.equals("left") && !side.equals("right")) {
            return null;
        }

        boolean isLeftSide = side.equals("left");
        String serviceName = isLeftSide ? leftServiceName : rightServiceName;
        boolean serviceRag = isLeftSide ? leftServiceRag : rightServiceRag;

        return switch (serviceName) {
            case "togetherai" -> serviceRag ? togetherAIService.chat(conversationId, message, prompt)
                    : togetherAIServiceNoRAG.chat(conversationId, message, prompt);
            case "openai" -> serviceRag ? openAIService.chat(conversationId, message,prompt)
                    : openAIServiceNoRAG.chat(conversationId, message, prompt);
            default -> null;
        };
    }


}
