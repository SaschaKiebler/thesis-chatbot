package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.DTOs.LectureDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.repositories.LectureRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/api/lectures")
@ApplicationScoped
public class LectureResource {

    @Inject
    LectureRepository lectureRepository;

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
