package de.htwg.chat.repositories;

import de.htwg.chat.entities.Answer;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AnswerRepository implements PanacheRepository<Answer> {

    public Uni<Answer> findByMessageId(UUID id) {
        return find("message.id", id).firstResult();
    }

    // find answer by message text and conversation id
    public Uni<Answer> findByMessageIdAndAnswerText(UUID messageId, String answerText) {
        return find("message.id = ?1 and answer = ?2", messageId, answerText).firstResult();
    }

}
