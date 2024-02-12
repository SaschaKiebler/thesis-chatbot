package de.htwg;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/llm")
@ApplicationScoped
public class LLMResource {

    @Inject
    OpenAIService openAIService;

    @Inject
    TogetherAIService togetherAIService;

    @Inject
    MessageRepository messageRepository;

    public LLMResource(OpenAIService openAIService, TogetherAIService togetherAIService) {
        this.openAIService = openAIService;
        this.togetherAIService = togetherAIService;

    }

    @POST
    @Path("/commercial")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String sendRequestCommercial(@QueryParam("message") String message) {
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }
        Message inputMessage = new Message.MessageBuilder()
                .message(message)
                .model(Modeltype.COMMERCIAL.toString())
                .build();
        messageRepository.persist(inputMessage);
        return openAIService.chat(message);
    }

    @POST
    @Path("/opensource")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendRequestOpenSource(@QueryParam("message") String message) {
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }

        return togetherAIService.chat(message);
    }

}
