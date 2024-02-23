package de.htwg.chat.repositories;

import de.htwg.chat.entities.SystemPrompt;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class SystemPromptRepository implements PanacheRepository<SystemPrompt> {

    public Uni<SystemPrompt> findByConversationId(UUID id) {
        return find("conversation.id", id).firstResult();
    }
}
