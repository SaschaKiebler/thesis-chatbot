package de.htwg.chat.repositories;

import de.htwg.chat.entities.SystemMessage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemMessageRepository implements PanacheRepository<SystemMessage>{
}
