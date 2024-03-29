package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;
import java.util.UUID;

@Path("/api/lectures")
@ApplicationScoped
public class LectureResource {

    @Inject
    LectureRepository lectureRepository;
    @Inject
    ScriptRepository scriptRepository;

    @Path("/all")
    @GET
    @Produces("application/json")
    public List<Lecture> getAllLectures() {
        return lectureRepository.listAll();
    }

    @Path("/{id}/scripts")
    @GET
    @Produces("application/json")
    public List<Script> getScriptsFromLecture(UUID id) {
        Lecture lecture = lectureRepository.findById(id);
        return scriptRepository.list("lecture", lecture);
    }

}
