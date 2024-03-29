package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.entities.MultipleChoiceQuestion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class MCQuizRepository implements PanacheRepository<MCQuiz> {


    public MCQuiz findById(UUID id) {
        return find("id", id).firstResult();
    }

}
