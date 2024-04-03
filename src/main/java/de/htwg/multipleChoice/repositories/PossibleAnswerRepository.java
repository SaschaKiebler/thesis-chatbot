package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.PossibleAnswer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class PossibleAnswerRepository implements PanacheRepository<PossibleAnswer> {

    public PossibleAnswer findById(UUID id) {
        return find("id", id).firstResult();
    }

}
