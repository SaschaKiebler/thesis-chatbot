package de.htwg.chat.repositories;

import de.htwg.chat.entities.SystemPrompt;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class SystemPromptRepository implements PanacheRepository<SystemPrompt>{

    public SystemPrompt findByConversationId(UUID id) {
        return find("conversation.id", id).firstResult();
    }
}
