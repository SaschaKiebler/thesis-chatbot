package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.repositories.LectureRepository;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * This class is the Resource for the "Multiple Choice View".
 * It serves the multipleChoice Template.
 */
@Path("/multipleChoice")
@ApplicationScoped
public class MultipleChoiceUIResource {

    @Inject
    Template multipleChoice;

    @Inject
    LectureRepository lectureRepository;

    /**
     * This method is called when a GET request is sent to /multipleChoice.
     * It returns the multipleChoice Template.
     * @return The multipleChoice Template.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance getMultipleChoice() {
        List<Lecture> lectures = lectureRepository.listAll();
        return multipleChoice.data("lectures", lectures.toArray());
    }
}
