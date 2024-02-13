package de.htwg.llms;

import de.htwg.chat.Answer;
import de.htwg.chat.AnswerRepository;
import de.htwg.chat.Message;
import de.htwg.chat.MessageRepository;
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

    @Inject
    AnswerRepository answerRepository;

    public LLMResource(OpenAIService openAIService, TogetherAIService togetherAIService) {
        this.openAIService = openAIService;
        this.togetherAIService = togetherAIService;

    }


    @POST
    @Path("/commercial")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String sendRequestCommercial(@QueryParam("message") String message) {
        // test for null or empty input
        if (message == null || message.isEmpty()) {
            return "Please provide a message";
        }
        // save the user message
        Message msg = new Message.MessageBuilder()
                .message(message)
                .model(Modeltype.COMMERCIAL.toString())
                .build();
        messageRepository.persist(msg);

        // get the answer from the AI
        String answer = openAIService.chat(message);

        // save the answer
        answerRepository.persist(new Answer.AnswerBuilder()
                .answer(answer)
                        .message(msg)
                .model(Modeltype.COMMERCIAL.toString())
                .build());
        return answer;
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
        String answer = openAIService.chat(message);
        // save the answer
        answerRepository.persist(new Answer.AnswerBuilder()
                .answer(answer)
                .message(msg)
                .model(Modeltype.OPEN_SOURCE.toString())
                .build());

        return answer;
    }

}
