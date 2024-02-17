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
}
