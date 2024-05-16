package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.Lecture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class LectureRepository implements PanacheRepository<Lecture> {

    public Lecture findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Lecture findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Lecture> findByIds(Set<UUID> ids) {
        return list("id in ?1", ids);
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
