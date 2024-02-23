package de.htwg.chat.repositories;

import de.htwg.chat.entities.Conversation;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ConversationRepository implements PanacheRepository<Conversation> {
    public Uni<Conversation> findById(UUID uuid) {
        return find("id", uuid).firstResult();
    }
}
