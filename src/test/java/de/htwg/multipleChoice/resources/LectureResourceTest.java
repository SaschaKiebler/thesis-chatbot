package de.htwg.multipleChoice.resources;

import de.htwg.multipleChoice.DTOs.LectureDTO;
import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.repositories.LectureRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class LectureResourceTest {


    @InjectMock
    LectureRepository lectureRepository;


    @Test
    void getAllLectures() {
        given()
                .when()
                .get("/api/lectures/all")
                .then()
                .statusCode(200);
    }

    @Test
    @TestTransaction
    void testAddLectureWithValidInputDTO() {
        LectureDTO lectureDTO = new LectureDTO("test", "test");
        doNothing().when(lectureRepository).persist(any(Lecture.class));
        given()
                .contentType("application/json")
                .body(lectureDTO)
                .post("/api/lectures/add")
                .then()
                .statusCode(200);
    }


}