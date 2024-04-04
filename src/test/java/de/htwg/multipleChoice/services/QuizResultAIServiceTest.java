package de.htwg.multipleChoice.services;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class QuizResultAIServiceTest {

    @Inject
    QuizResultAIService quizResultAIService;
    @InjectMock
    ConversationRepository conversationRepository;

    @Test
    void sendQuizAndValidUserRequest() {
        UUID uuid = UUID.randomUUID();
        Conversation conversation = new Conversation();
        conversation.setId(uuid);
        when(conversationRepository.findById(uuid)).thenReturn(conversation);

    }
}