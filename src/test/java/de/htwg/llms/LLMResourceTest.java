package de.htwg.llms;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.entities.Message;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.llms.services.OpenAIService;
import de.htwg.chat.llms.services.OpenAIServiceNoRAG;
import de.htwg.chat.llms.services.TogetherAIService;
import de.htwg.chat.llms.services.TogetherAIServiceNoRAG;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
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

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("test");

        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);
        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));


        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/leftService?conversationId=" + conversation.getId())
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

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("test");

        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));


        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/rightService?conversationId=" + conversation.getId())
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

    @Test
    void testSendRequestLeftServiceForAiNotResponding() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

            when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);

            when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);
        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("");
        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/leftService?conversationId=" + conversation.getId())
                .then()
                .body(is("{\"error\":\"etwas ist mit der KI schiefgelaufen versuche es später nochmal...\"}"));
    }

    @Test
    void testSendRequestRightServiceForAiNotResponding() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);
        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(message);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("");

        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/rightService?message=test&conversationId=" + conversation.getId())
                .then()
                .body(is("{\"error\":\"etwas ist mit der KI schiefgelaufen versuche es später nochmal...\"}"));

    }

    @Test
    void testSendRequestLeftServiceForDbNotResponding() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(null);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);
        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(null);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("test");

        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/leftService?conversationId=" + conversation.getId())
                .then()
                .body(is("{\"error\":\"etwas ist mit der DB schiefgelaufen versuche es später nochmal...\"}"));

    }

    @Test
    void testSendRequestRightServiceForDbNotResponding() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = Message.builder().message("test").conversation(conversation).id(UUID.randomUUID()).build();
        Answer answer = Answer.builder().answer("test").message(message).id(UUID.randomUUID()).build();

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(null);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);
        when(messageRepository.findLatestMessageFromConversation(any(UUID.class))).thenReturn(null);

        when(answerRepository.findByMessageId(any(UUID.class))).thenReturn(answer);

        doNothing().when(conversationRepository).persist(any(Conversation.class));
        doNothing().when(messageRepository).persist(any(Message.class));
        doNothing().when(answerRepository).persist(any(Answer.class));

        when(openAIService.chat(anyString(),anyString(),anyString())).thenReturn("test");
        when(openAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIServiceNoRAG.chat(anyString(),anyString(), anyString())).thenReturn("test");
        when(togetherAIService.chat(anyString(),anyString(), anyString())).thenReturn("test");

        given().contentType(ContentType.JSON).body("{\"message\":\"test\"}")
                .when().post("/llm/rightService?message=test&conversationId=" + conversation.getId())
                .then()
                .body(is("{\"error\":\"etwas ist mit der DB schiefgelaufen versuche es später nochmal...\"}"));

    }



}