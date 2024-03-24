package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

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
     * @param id The id of the multiple choice question.
     * @return The multiple choice question.
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String getMultipleChoice(@FormParam("id") String id) {
        Conversation conversation = new Conversation();
        conversationRepository.persist(conversation);

        return multipleChoiceAIService.getQuestion(id, conversation.getId().toString());
    }
}
