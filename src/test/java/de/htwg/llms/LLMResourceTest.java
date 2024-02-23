package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import dev.langchain4j.service.OnCompleteOrOnError;
import dev.langchain4j.service.TokenStream;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

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

    @InjectMock
    ConversationRepository conversationRepository;



    @Test
    void testSendRequestCommercialForValidStringWithMockedService() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class)).await().indefinitely()).thenReturn(conversation);
        when(openAIService.chat(anyString(),eq("test"))).thenReturn("test");
        List<Message> messages = List.of(message);
        when(messageRepository.findByConversationIdAndMessage(any(UUID.class),anyString()).await().indefinitely()).thenReturn(messages);
        when(answerRepository.findByMessageIdAndAnswerText(any(UUID.class),eq("test")).await().indefinitely()).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));


        given()
                .when().post("/llm/commercial?message=test&conversationId=" + conversation.getId())
                .then()
                .statusCode(200)
                .body(
                        is("{\"answer\":\"test\",\"conversationId\":\""
                                + conversation.getId()
                                + "\"}"));

    }

    @Test
    void testSendRequestOpenSourceForValidStringWithMockedService() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());

        when(conversationRepository.findById(any(UUID.class)).await().indefinitely()).thenReturn(conversation);
        when(togetherAIService.chat(anyString(),eq("test"))).thenReturn("test");
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        given()
                .when().post("/llm/opensource?message=test&conversationId=" + conversation.getId())
                .then()
                .statusCode(200)
                .body(
                        is("{\"answer\":\"test\",\"conversationId\":\""
                                + conversation.getId()
                                + "\"}"));


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