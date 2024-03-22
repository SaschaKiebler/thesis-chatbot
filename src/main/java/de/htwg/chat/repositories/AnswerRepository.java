package de.htwg.chat.repositories;

import de.htwg.chat.entities.Answer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

/**
 * This class is a custom implementation of the PanacheRepository interface to extend functionality.
 */
@ApplicationScoped
public class AnswerRepository implements PanacheRepository<Answer> {

    /**
     * This method is used to find an answer by message id.
     * It returns an answer.
     *
     * @param id the message id
     * @return an answer
     */
    public Answer findByMessageId(UUID id) {
        return find("message.id", id).firstResult();
    }

    /**
     * This method is used to find an answer by message id and answer text.
     * It returns an answer.
     *
     * @param messageId the message id
     * @param answerText the answer text
     * @return an answer
     */
    public Answer findByMessageIdAndAnswerText(UUID messageId, String answerText, UUID conversationId) {
        return find("answer = ?2 and message.id = ?1 and message.conversation.id = ?3 ", messageId, answerText, conversationId).firstResult();
    }

    /**
     * This method is used to set the preferred answer.
     *
     * @param id the answer id
     * @param value the value
     */
    public void setPreferred(UUID id, boolean value) {
        update("preferred = ?1 where id = ?2", value, id);
    }

    /**
     * This method is used to find an answer by id.
     * It returns an answer.
     *
     * @param id the answer id
     * @return an answer
     */
    public Answer findById(UUID id) {
        return find("id", id).firstResult();
    }

    /**
     * This method is used to set the preferred cause.
     *
     * @param uuid the answer id
     * @param value the value
     */
    public void setPreferredCause(UUID uuid, String value) {
        update("cause = ?1 where id = ?2", value, uuid);
    }
}
