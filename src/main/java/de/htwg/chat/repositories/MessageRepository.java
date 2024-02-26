package de.htwg.chat.repositories;

import de.htwg.chat.entities.Message;
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
        List<Message> messages = find("conversation.id", id).list();
        messages.sort(Comparator.comparing(Message::getDate));
        return find("conversation.id", id).list();
    }


    public Message findLatestMessageFromConversation(UUID conversationId) {
        List<Message> messages = find("conversation.id = ?1 order by date DESC", conversationId).list();
        if (messages.isEmpty())
            return null;

        return messages.get(messages.size()-1);
    }



}
