package de.htwg.chat.repositories;

import de.htwg.chat.entities.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * This class is a custom implementation of the PanacheRepository interface.
 * to provide more functionality.
 */
@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {

    /**
     * This method is used to find a message by id.
     * It uses the id.
     * It returns a message.
     *
     * @param id the message id
     * @return a message
     */
    public Message findById(UUID id) {
        return find("id", id).firstResult();
    }

    /**
     * This method is used to find a message by conversation id.
     * It uses the conversation id.
     * It returns a list of messages.
     *
     * @param id the conversation id
     * @return a list of messages
     */
    public List<Message> findByConversationId(UUID id) {
        return find("conversation.id = ?1 order by date asc", id).list();
    }


    /**
     * This method is used to find the latest message from a conversation.
     * It uses the conversation id.
     * It returns a message.
     *
     * @param conversationId the conversation id
     * @return a message
     */
    public Message findLatestMessageFromConversation(UUID conversationId) {
        List<Message> messages = find("conversation.id = ?1 order by date DESC", conversationId).list();
        if (messages.isEmpty())
            return null;

        return messages.get(0);
    }



}
