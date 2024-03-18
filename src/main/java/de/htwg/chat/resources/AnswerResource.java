package de.htwg.chat.resources;

import de.htwg.chat.repositories.AnswerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.UUID;

@Path("/api/answer")
@ApplicationScoped
public class AnswerResource {

    @Inject
    AnswerRepository answerRepository;

    @PUT
    @Path("/preffered")
    @Transactional
    public void put(@QueryParam("value") String value, @QueryParam("id") String id) {
        if (value.equals("true")) {
            answerRepository.setPreferred(UUID.fromString(id), true);
            System.out.println("Set preferred to true on answer with id: " + id);
        }
        else {
            System.out.println("Something went wrong with rating for answer with id: " + id);
        }
    }

    @PUT
    @Path("/preffered/cause")
    @Transactional
    public void putCause(@QueryParam("value") String value, @QueryParam("id") String id) {
        try {
            answerRepository.setPreferredCause(UUID.fromString(id), value);
            System.out.println("Set preferred cause to "+ value +" on answer with id: " + id);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with setting preferred cause for answer with id: " + id);
        }

    }
}
