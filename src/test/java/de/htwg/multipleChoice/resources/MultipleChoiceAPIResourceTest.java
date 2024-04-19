package de.htwg.multipleChoice.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MultipleChoiceAPIResourceTest {

    @Test
    void testGetQuizForExistingQuiz() {

    }


    @Test
    void testGetStudentResults() {
        given()
                .when()
                .get("/api/quiz/results" + "/" + "4729aad1-45eb-493d-b3eb-d25e9598f3ce")
                .then()
                .statusCode(200);
    }
}