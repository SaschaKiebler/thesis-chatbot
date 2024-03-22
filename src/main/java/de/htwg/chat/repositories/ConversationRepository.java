package de.htwg.chat.repositories;

import de.htwg.chat.entities.Conversation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

/**
 * This class is a custom implementation of the PanacheRepository interface.
 * It is used to extend the PanacheRepository to provide more functionality.
 */
@ApplicationScoped
public class ConversationRepository implements PanacheRepository<Conversation> {

    /**
     * This method is used to find a conversation by id.
     * It uses the id.
     * It returns a conversation.
     *
     * @param uuid the conversation id
     * @return a conversation
     */
    public Conversation findById(UUID uuid) {
        return find("id", uuid).firstResult();
    }
}
