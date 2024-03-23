package de.htwg.multipleChoice;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LectureRepository implements PanacheRepository<Lecture> {

    public Lecture findById(UUID id) {
        return find("id", id).firstResult();
    }
}
