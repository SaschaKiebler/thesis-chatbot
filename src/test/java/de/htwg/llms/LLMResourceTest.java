package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.llms.services.OpenAIService;
import de.htwg.llms.services.OpenAIServiceNoRAG;
import de.htwg.llms.services.TogetherAIService;
import de.htwg.llms.services.TogetherAIServiceNoRAG;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


import java.util.UUID;

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
    OpenAIServiceNoRAG openAIServiceNoRAG;

    @InjectMock
    TogetherAIServiceNoRAG togetherAIServiceNoRAG;

    @InjectMock
    MessageRepository messageRepository;

    @InjectMock
    AnswerRepository answerRepository;

    @InjectMock
    ConversationRepository conversationRepository;




    @Test
    void testSendRequestLeftServiceForValidString() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        when(openAIService.chat(anyString(),eq("test"))).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),eq("test"))).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),eq("test"))).thenReturn("test");
        when(togetherAIService.chat(anyString(),eq("test"))).thenReturn("test");

        when(messageRepository.findByConversationIdAndMessage(any(UUID.class),anyString())).thenReturn(message);
        when(answerRepository.findByMessageIdAndAnswerText(any(UUID.class),anyString())).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));


        given()
                .when().post("/llm/leftService?message=test&conversationId=" + conversation.getId())
                .then()
                .statusCode(200)
                .body(
                        is(is("{\"answer\":\"test\",\"conversationId\":\""
                                        + conversation.getId()
                                        + "\",\"answerId\":\""
                                        + answer.getId()
                                        + "\"}")));

    }

    @Test
    void testSendRequestRightServiceForValidString() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        when(openAIService.chat(anyString(),eq("test"))).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),eq("test"))).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),eq("test"))).thenReturn("test");
        when(togetherAIService.chat(anyString(),eq("test"))).thenReturn("test");

        when(messageRepository.findByConversationIdAndMessage(any(UUID.class),anyString())).thenReturn(message);
        when(answerRepository.findByMessageIdAndAnswerText(any(UUID.class),anyString())).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));


        given()
                .when().post("/llm/rightService?message=test&conversationId=" + conversation.getId())
                .then()
                .statusCode(200)
                .body(
                        is(is("{\"answer\":\"test\",\"conversationId\":\""
                                + conversation.getId()
                                + "\",\"answerId\":\""
                                + answer.getId()
                                + "\"}")));

    }

    @Test
    void testSendRequestLeftServiceForEmptyString() {
        given()
                .when().post("/llm/leftService")
                .then()
                .body(is("{\"error\":\"bitte gebe eine Nachricht ein\"}"));
    }

    @Test
    void testSendRequestRightServiceForEmptyString() {
        given()
                .when().post("/llm/rightService")
                .then()
                .body(is("{\"error\":\"bitte gebe eine Nachricht ein\"}"));
    }

    @Test
    void testSendRequestLeftServiceForNullStringMessage() {
        given()
                .when().post("/llm/leftService?message=")
                .then()
                .body(is("{\"error\":\"bitte gebe eine Nachricht ein\"}"));
    }

    @Test
    void testSendRequestRightServiceForNullStringMessage() {
        given()
                .when().post("/llm/rightService?message=")
                .then()
                .body(is("{\"error\":\"bitte gebe eine Nachricht ein\"}"));
    }



}