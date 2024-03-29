package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.MultipleChoiceQuestion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MultipleChoiceQuestionRepository implements PanacheRepository<MultipleChoiceQuestion> {

}
