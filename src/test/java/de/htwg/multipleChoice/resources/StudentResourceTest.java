package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.DTOs.StudentDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.StudentRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class StudentResourceTest {

    @Inject
    StudentRepository studentRepository;

    @Inject
    LectureRepository lectureRepository;

    private UUID studentId;
    private UUID lectureId;

    @BeforeEach
    @Transactional
    public void setup() {
        Lecture lecture = new Lecture("test", "test");
        lectureRepository.persist(lecture);
        // Create a student for testing
        Student student = new Student("testerMester", List.of(lecture));
        studentRepository.persist(student);
        studentId = student.getId();
        lectureId = lecture.getId();
    }

    @Test
    public void testGetNewStudentId() {
        given()
                .when().get("/api/student/id")
                .then()
                .statusCode(200)
                .body("studentId", notNullValue());
    }

    @Test
    @TestTransaction
    public void testGetStudentData() {
        given()
                .pathParam("id", studentId.toString())
                .when().get("/api/student/data/{id}")
                .then()
                .statusCode(200)
                .body("id", is(studentId.toString()));
    }

    @Test
    @TestTransaction
    public void testUpdateStudentData() {
        StudentDTO studentDTO = new StudentDTO("Updated Name");
        studentDTO.setLectures(Collections.emptyList());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", studentId.toString())
                .body(studentDTO)
                .when().put("/api/student/data/{id}")
                .then()
                .statusCode(200);

        Student updatedStudent = studentRepository.findById(studentId);
        assert updatedStudent != null;
        assert "Updated Name".equals(updatedStudent.getName());
    }

    @Test
    @TestTransaction
    public void testDeleteLecture() {

        given()
                .pathParam("studentId", studentId.toString())
                .pathParam("lectureId", lectureId.toString())
                .when().delete("/api/student/lecture/{studentId}/{lectureId}")
                .then()
                .statusCode(200);
    }
}