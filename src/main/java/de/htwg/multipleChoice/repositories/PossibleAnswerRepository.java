package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.PossibleAnswer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PossibleAnswerRepository implements PanacheRepository<PossibleAnswer> {
}
