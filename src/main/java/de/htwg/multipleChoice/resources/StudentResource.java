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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        System.out.println("Deleting lecture with id: " + lectureId + " from student with id: " + studentId);
        try {
            UUID studentUUID = UUID.fromString(studentId);
            UUID lectureUUID = UUID.fromString(lectureId);

            Student student = studentRepository.findById(studentUUID);
            if (student == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
            }

            Lecture lecture = lectureRepository.findById(lectureUUID);
            if (lecture == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Lecture not found").build();
            }

            if (!student.getLectures().contains(lecture)) {
                return Response.status(Response.Status.NOT_FOUND).entity("Lecture not associated with student").build();
            }

            student.getLectures().remove(lecture);

            studentRepository.persist(student);

            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID format").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error removing lecture from student").build();
        }
    }


    @Path("/data/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public synchronized Response updateStudentData(@PathParam("id") String id, @RequestBody StudentDTO studentDTO) {
        System.out.println("Updating student with id: " + id);
        try {
            Student student = studentRepository.findById(UUID.fromString(id));

            if (student == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            // Update lectures
            if (studentDTO.getLectures() != null) {
                Set<UUID> lectureIds = studentDTO.getLectures().stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toSet());

                List<Lecture> newLectures = lectureRepository.findByIds(lectureIds);

                // Add new lectures, avoiding duplicates
                newLectures.forEach(lecture -> {
                    if (!student.getLectures().contains(lecture)) {
                        student.getLectures().add(lecture);
                    }
                });
            }

            // Update name
            if (studentDTO.getName() == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if (!studentDTO.getName().equals(student.getName())) {
                student.setName(studentDTO.getName());
                studentRepository.persist(student);
                System.out.println("Updated student: " + student.toString());
            } else {
                System.out.println("No changes in student: " + student.toString());
            }

            return Response.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
