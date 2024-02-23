package de.htwg.chat.repositories;

import de.htwg.chat.entities.Message;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {


    public Uni<Message> findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Uni<List<Message>> findByConversationId(UUID id) {

        return find("conversation.id", id).list();
    }

    public Uni<List<Message>> findByConversationIdAndMessage(UUID conversationId, String message) {
        //latest message (last result)
        return find("conversation.id = ?1 and message like '"+ message +"%'", conversationId).list();
    }



}
