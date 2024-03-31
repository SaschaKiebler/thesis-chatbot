package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.DTOs.LectureDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestForm;

import java.util.List;
import java.util.UUID;

@Path("/api/lectures")
@ApplicationScoped
public class LectureResource {

    @Inject
    LectureRepository lectureRepository;
    @Inject
    ScriptRepository scriptRepository;

    /**
     * Get all lectures
     * @return List of all lectures
     */
    @Path("/all")
    @GET
    @Produces("application/json")
    public List<Lecture> getAllLectures() {
        return lectureRepository.listAll();
    }

    /**
     * Get all scripts from a lecture
     * @param id UUID of the lecture
     * @return List of all scripts from the lecture
     */
    @Path("/{id}/scripts")
    @GET
    @Produces("application/json")
    public List<Script> getScriptsFromLecture(UUID id) {
        Lecture lecture = lectureRepository.findById(id);
        return scriptRepository.list("lecture", lecture);
    }

    /**
     *  Add a new lecture
     * @param lectureDTO
     * @return the created lecture
     */

    @Path("/add")
    @POST
    @Produces("application/json")
    @Transactional
    public Lecture addLecture(@RequestBody LectureDTO lectureDTO) {
        System.out.println("Adding lecture with name: " + lectureDTO.getName() + " and description: " + lectureDTO.getDescription());
        Lecture lecture = Lecture.builder()
                .name(lectureDTO.getName())
                .description(lectureDTO.getDescription())
                .build();
        lectureRepository.persist(lecture);
        return lecture;

    }

}
