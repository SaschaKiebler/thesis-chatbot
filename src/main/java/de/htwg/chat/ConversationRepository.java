package de.htwg.chat;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ConversationRepository implements PanacheRepository<Conversation> {
    public Conversation findById(UUID uuid) {
        return find("id", uuid).firstResult();
    }
}
