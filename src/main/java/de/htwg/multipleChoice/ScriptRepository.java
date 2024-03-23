package de.htwg.multipleChoice;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ScriptRepository implements PanacheRepository<Script> {

        public Script findById(UUID id) {
            return find("id", id).firstResult();
        }
}
