package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.Script;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ScriptRepository implements PanacheRepository<Script> {

        public Script findById(UUID id) {
            return find("id", id).firstResult();
        }

        public Script findByName(String name) {
            return find("name", name).firstResult();
        }
}
