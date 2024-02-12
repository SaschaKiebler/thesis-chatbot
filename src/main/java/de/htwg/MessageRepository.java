package de.htwg;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {


    public Message findById(UUID id) {
        return find("id", id).firstResult();
    }


}
