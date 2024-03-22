package de.htwg.chat.repositories;

import de.htwg.chat.entities.SystemPrompt;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

/**
 * This class is a custom implementation of the PanacheRepository interface
 * to provide more functionality.
 */
@ApplicationScoped
public class SystemPromptRepository implements PanacheRepository<SystemPrompt>{

    /**
     * This method is used to find a SystemPrompt by conversation id.
     * It uses the conversation id.
     * It returns a SystemPrompt.
     *
     * @param id the conversation id
     * @return a SystemPrompt
     */
    public SystemPrompt findByConversationId(UUID id) {
        return find("conversation.id", id).firstResult();
    }
}
