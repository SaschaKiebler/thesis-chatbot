package de.htwg;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.Mockito.when;

@QuarkusTest
class LLMResourceTest {

    @InjectMock
    OpenAIService openAIService;

    @InjectMock
    TogetherAIService togetherAIService;



    @Test
    void testSendRequestCommercialForValidStringWithMockedService() {
        when(openAIService.chat("test")).thenReturn("test");
        given()
                .when().post("/llm/commercial?message=test")
                .then()
                .statusCode(200)
                .body(is("test"));
    }

    @Test
    void testSendRequestOpenSourceForValidStringWithMockedService() {
        when(togetherAIService.chat("test")).thenReturn("test");
        given()
                .when().post("/llm/opensource?message=test")
                .then()
                .statusCode(200)
                .body(is("test"));
    }

    @Test
    void testSendRequestOpenSourceForEmptyStringWithMockedService() {
        given()
                .when().post("/llm/opensource")
                .then()
                .body(is("Please provide a message"));
    }

    @Test
    void testSendRequestCommercialForEmptyStringWithMockedService() {
        given()
                .when().post("/llm/commercial")
                .then()
                .body(is("Please provide a message"));
    }

    @Test
    void testSendRequestCommercialForNullStringWithMockedService() {
        given()
                .when().post("/llm/commercial?message=")
                .then()
                .body(is("Please provide a message"));
    }

    @Test
    void testSendRequestOpenSourceForNullStringWithMockedService() {
        given()
                .when().post("/llm/opensource?message=")
                .then()
                .body(is("Please provide a message"));
    }


}