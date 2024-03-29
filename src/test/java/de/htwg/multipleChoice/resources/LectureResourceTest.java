package de.htwg.multipleChoice.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LectureResourceTest {

    @Test
    void getAllLectures() {
        given()
                .when()
                .get("/api/lectures/all")
                .then()
                .statusCode(200);
    }

    @Test
    void getScriptsFromLecture() {
        given()
                .when()
                .get("/api/lectures/c159158e-2ca4-4d86-9b35-1b33fa7654b9/scripts")
                .then()
                .statusCode(200);
    }

    @Test
    void getScriptsFromLectureNotFound() {
        given()
                .when()
                .get("/api/lectures//scripts")
                .then()
                .statusCode(404);
    }

}