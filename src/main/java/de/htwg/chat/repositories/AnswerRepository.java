package de.htwg.chat.repositories;

import de.htwg.chat.entities.Answer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AnswerRepository implements PanacheRepository<Answer> {

    public Answer findByMessageId(UUID id) {
        return find("message.id", id).firstResult();
    }

    // find answer by message text and conversation id
    public Answer findByMessageIdAndAnswerText(UUID messageId, String answerText) {
        return find("message.id = ?1 and answer = ?2", messageId, answerText).firstResult();
    }

}
