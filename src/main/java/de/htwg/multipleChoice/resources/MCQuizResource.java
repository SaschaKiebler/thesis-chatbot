package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Path("{id}/result")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getQuizResult(@PathParam("id") String id, @RequestBody MCQuiz givenAnswers) {
        try {
            MCQuiz quiz = mcQuizRepository.findById(UUID.fromString(id));
            Map<String, String> result = new HashMap<>();
            result.put("result", quiz.getCorrectAnswers().equals(givenAnswers.getCorrectAnswers()) ? "Correct" : "Wrong");
            return Response.ok(result).build();

        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
