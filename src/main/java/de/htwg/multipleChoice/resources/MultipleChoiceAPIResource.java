package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.services.MultipleChoiceAIService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/multipleChoice")
@ApplicationScoped
public class MultipleChoiceAPIResource {

    @Inject
    MultipleChoiceAIService multipleChoiceAIService;


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
    public Response produceQuiz(@FormParam("message") String message, @FormParam("conversationId") String conversationId){
        if (conversationId == null || conversationId.isEmpty() || message == null || message.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("conversationId and message must not be empty").build();
        }
        return Response.ok().entity(multipleChoiceAIService.getQuestion(message, conversationId)).build();
    }
}
