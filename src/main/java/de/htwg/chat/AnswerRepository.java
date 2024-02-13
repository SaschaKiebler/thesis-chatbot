package de.htwg.chat;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnswerRepository implements PanacheRepository<Answer> {

}
