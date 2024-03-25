package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.services.MultipleChoiceAIService;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

@Path("/api/multipleChoice")
@ApplicationScoped
public class MultipleChoiceAPIResource {

    @Inject
    MultipleChoiceAIService multipleChoiceAIService;

    @Inject
    ConversationRepository conversationRepository;


    /**
     * This method is called when a GET request is sent to /api/multipleChoice.
     * It returns the multiple choice question with the given id.
     *
     * @param message The user-message.
     * @param conversationId The conversation id.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response produceQuiz(@RequestBody(ref = "message") String message, @QueryParam("conversationId") String conversationId){
        Conversation conversation;
        if (conversationId == null || conversationId.isEmpty() || message == null) {
            conversation = new Conversation();
            conversationRepository.persist(conversation);
        }
        else {
            conversation = conversationRepository.findById(UUID.fromString(conversationId));
        }

        String answer = multipleChoiceAIService.getQuestion(message, conversation.getId().toString());

        return Response.ok().entity(Json.object().put("answer",answer).put("conversationId",conversation.getId().toString()).build()).build();
    }
}
