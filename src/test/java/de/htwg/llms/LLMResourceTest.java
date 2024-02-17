package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.MessageRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class LLMResourceTest {

    @InjectMock
    OpenAIService openAIService;

    @InjectMock
    TogetherAIService togetherAIService;

    @InjectMock
    MessageRepository messageRepository;

    @InjectMock
    AnswerRepository answerRepository;



    @Test
    void testSendRequestCommercialForValidStringWithMockedService() {
        when(openAIService.chat("test")).thenReturn("test");
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        given()
                .when().post("/llm/commercial?message=test")
                .then()
                .statusCode(200);

        verify(messageRepository, times(1)).persist(any(Message.class));
        verify(answerRepository, times(1)).persist(any(Answer.class));
    }

    @Test
    void testSendRequestOpenSourceForValidStringWithMockedService() {
        when(togetherAIService.chat("test")).thenReturn("test");
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        given()
                .when().post("/llm/opensource?message=test")
                .then()
                .statusCode(200)
                .body(is("test"));

        verify(messageRepository, times(1)).persist(any(Message.class));
        verify(answerRepository, times(1)).persist(any(Answer.class));
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