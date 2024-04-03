package de.htwg.chat.repositories;

import de.htwg.chat.entities.SimpleChatMessage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SimpleChatMessageRepository implements PanacheRepository<SimpleChatMessage> {

    public List<SimpleChatMessage> findByConversationId(UUID id) {
        return find("conversation.id = ?1", id).list();
    }

    public void saveAll(List<SimpleChatMessage> messages) {
        for (SimpleChatMessage message : messages) {
            persist(message);
        }
    }

    public void deleteByConversationId(UUID memoryIdUUID) {
        List<SimpleChatMessage> messages = findByConversationId(memoryIdUUID);
        for (SimpleChatMessage message : messages) {
            delete(message);
        }
    }
}
