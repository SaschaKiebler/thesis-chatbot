package de.htwg.chat.resources;

import de.htwg.chat.repositories.AnswerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.UUID;

/**
 * This class is the Resource for the Answer entity.
 * It has Endpoints for the Rating and the Cause.
 * It uses the AnswerRepository to access the database.
 */
@Path("/api/answer")
@ApplicationScoped
public class AnswerResource {

    @Inject
    AnswerRepository answerRepository;

    /**
     * This method is called when a PUT request is sent to /api/answer/preffered.
     * It sets the preferred value of an answer to true if the value==true.
     * @param value The value of the preferred field.
     * @param id The id of the answer.
     */
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

    /**
     * This method is called when a PUT request is sent to /api/answer/preffered/cause.
     * It sets the cause of why an answer is preferred.
     * @param value The value of the preferred cause field.
     * @param id The id of the answer.
     */
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
