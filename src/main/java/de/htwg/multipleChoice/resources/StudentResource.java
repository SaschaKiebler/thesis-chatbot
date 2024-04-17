package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.DTOs.StudentDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.StudentRepository;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

@Path("/api/student")
@ApplicationScoped
public class StudentResource {

    @Inject
    private StudentRepository studentRepository;
    @Inject
    private LectureRepository lectureRepository;


    @Path("/id")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getNewStudentId() {
        Student student = new Student();
        try {
            studentRepository.persist(student);
            return Response.ok(Json.object().put("studentId", student.getId().toString()).build()).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }
    }

    @Path("/data/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentData(@PathParam("id") String id) {
        try {
            Student student = studentRepository.findById(UUID.fromString(id));
            System.out.println(student.toString());
            return Response.ok(student).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/lecture/{studentId}/{lectureId}")
    @DELETE
    @Transactional
    public Response deleteLecture(@PathParam("studentId") String studentId, @PathParam("lectureId") String lectureId) {
        try {
            Student student = studentRepository.findById(UUID.fromString(studentId));
            studentRepository.deleteLecture(student.getId(), UUID.fromString(lectureId));
            return Response.ok().build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/data/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateStudentData(@PathParam("id") String id, @RequestBody StudentDTO studentDTO) {
        try {
            Student student = studentRepository.findById(UUID.fromString(id));

            if(studentDTO.getLectures() != null) {
                for (String lectureId : studentDTO.getLectures()) {
                    Lecture lecture = lectureRepository.findById(UUID.fromString(lectureId));
                    if (student.getLectures().contains(lecture)) {
                        continue;
                    }
                    student.getLectures().add(lecture);
                }
                studentRepository.persist(student);
            }

            if (studentDTO.getName() == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (student.getName()!=null) {
                if (student.getName().equals(studentDTO.getName())) {
                System.out.println("No changes in student: " + student.toString());
                return Response.ok().build();
                }
            }
            student.setName(studentDTO.getName());
            studentRepository.persist(student);
            System.out.println("Updated student: " + student.toString());
            return Response.ok().build();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

}
