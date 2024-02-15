package de.htwg.llms;

import com.fasterxml.jackson.databind.util.JSONPObject;
import de.htwg.chat.*;
import dev.langchain4j.service.AiServices;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/llm")
@ApplicationScoped
public class LLMResource {

    @Inject
    OpenAIService openAIService;

    @Inject
    TogetherAIService togetherAIService;

    @Inject
    MessageRepository messageRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    ConversationRepository conversationRepository;


    public LLMResource(OpenAIService openAIService, TogetherAIService togetherAIService) {
        this.openAIService = openAIService;
        this.togetherAIService = togetherAIService;

    }


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
        Conversation conversation = new Conversation();
        if (conversationId != null && !conversationId.isEmpty()) {
            conversation = conversationRepository.findById(UUID.fromString(conversationId));
        }

        conversationRepository.persist(conversation);

        // save the user message
        Message msg = new Message.MessageBuilder()
                .message(message)
                .model(Modeltype.COMMERCIAL.toString())
                .conversation(conversation)
                .build();
        messageRepository.persist(msg);

        // get the answer from the AI
        String answer = openAIService.chat(message);

        if (answer == null) {
            return "Sorry, the service is currently not available. Please try again later.";
        }

        // save the answer
        answerRepository.persist(new Answer.AnswerBuilder()
                .answer(answer)
                .message(msg)
                .model(Modeltype.COMMERCIAL.toString())
                .build());
        return Json.object().put("answer",answer).put("conversationId", conversation.getId().toString()).build();

    }

    @POST
    @Path("/opensource")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String sendRequestOpenSource(@QueryParam("message") String message) {
        // test for null or empty input
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }

        // save the user message
        Message msg = new Message.MessageBuilder()
                .message(message)
                .model(Modeltype.OPEN_SOURCE.toString())
                .build();
        messageRepository.persist(msg);

        // get the answer from the AI
        String answer = togetherAIService.chat(message);

        if (answer == null) {
            return "Sorry, the service is currently not available. Please try again later.";
        }
        // save the answer
        answerRepository.persist(new Answer.AnswerBuilder()
                .answer(answer)
                .message(msg)
                .model(Modeltype.OPEN_SOURCE.toString())
                .build());

        return answer;
    }

}
