package de.htwg.chat;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {


    public Message findById(UUID id) {
        return find("id", id).firstResult();
    }

    public List<Message> findByConversationId(UUID id) {
        List<Message> messages = find("conversationId", id).list();
        messages.sort(Comparator.comparing(Message::getDate));
        return find("conversationId", id).list();
    }



}
