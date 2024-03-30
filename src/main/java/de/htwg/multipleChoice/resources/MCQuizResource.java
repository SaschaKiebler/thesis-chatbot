package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("api/quiz")
public class MCQuizResource {

    @Inject
    MCQuizRepository mcQuizRepository;

    /**
     * Retrieves a quiz by its ID.
     *
     * @param  id  the ID of the quiz to retrieve
     * @return     the quiz with the specified ID, or a NOT_FOUND response if the quiz does not exist
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuiz(@PathParam("id") String id) {
        try {
            MCQuiz quiz = mcQuizRepository.findById(UUID.fromString(id));
            return Response.ok(quiz).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
