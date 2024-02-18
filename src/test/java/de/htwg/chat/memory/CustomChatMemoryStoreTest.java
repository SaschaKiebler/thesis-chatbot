package de.htwg.chat.memory;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.Message;
import de.htwg.chat.entities.SystemPrompt;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.repositories.SystemPromptRepository;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class CustomChatMemoryStoreTest {

    @InjectMock
    MessageRepository messageRepository;

    @InjectMock
    AnswerRepository answerRepository;

    @InjectMock
    ConversationRepository conversationRepository;

    @InjectMock
    SystemPromptRepository systemPromptRepository;

    @Inject
    CustomChatMemoryStore customChatMemoryStore;


    @Test
    void testGetMessagesForDefaultMemoryId() {

        assertEquals(0, customChatMemoryStore.getMessages("default").size());
    }

    @Test
    void testGetMessagesForEmptyMemoryId() {
        assertEquals(0, customChatMemoryStore.getMessages("").size());
    }

    @Test
    void testUpdatedMessagesForEmptyList() {
        Conversation conversation = new Conversation();
        conversation.setId(java.util.UUID.randomUUID());

        doNothing().when(conversationRepository).persist(conversation);
        when(conversationRepository.findById(conversation.getId())).thenReturn(conversation);
        when(messageRepository.findByConversationId(any(UUID.class))).thenReturn(List.of());

        customChatMemoryStore.updateMessages(conversation.getId().toString(), List.of());

        assertEquals(0, customChatMemoryStore.getMessages(conversation.getId().toString()).size());
    }

    @Test
    void testUpdatedMessagesForListOfOneAndOneMessageInTheConversation() {
        Conversation conversation = new Conversation();
        conversation.setId(java.util.UUID.randomUUID());

        doNothing().when(conversationRepository).persist(conversation);
        when(conversationRepository.findById(conversation.getId())).thenReturn(conversation);
        when(messageRepository.findByConversationId(any(UUID.class))).thenReturn(List.of(new Message.MessageBuilder().message("test").conversation(conversation).build()));

        customChatMemoryStore.updateMessages(conversation.getId().toString(), List.of(new UserMessage("test,test")));
        verify(messageRepository, times(1)).persist(any(Message.class));
        System.out.println(customChatMemoryStore.getMessages(conversation.getId().toString()));
        assertEquals(1, customChatMemoryStore.getMessages(conversation.getId().toString()).size());
    }

    @Test
    void testUpdatedMessagesForListOfOneAndNoMessageInTheConversation() {
        Conversation conversation = new Conversation();
        conversation.setId(java.util.UUID.randomUUID());

        doNothing().when(conversationRepository).persist(conversation);
        when(conversationRepository.findById(conversation.getId())).thenReturn(conversation);
        when(messageRepository.findByConversationId(any(UUID.class))).thenReturn(List.of());

        customChatMemoryStore.updateMessages(conversation.getId().toString(), List.of(new UserMessage("test,test")));
        verify(messageRepository, times(1)).persist(any(Message.class));
        //expecting 0 messages because the db is mocked
        assertEquals(0, customChatMemoryStore.getMessages(conversation.getId().toString()).size());
    }

    @Test
    void testUpdatedMessagesForListOfOneAndTwoMessageInTheConversationWithSystemMessage() {
        Conversation conversation = new Conversation();
        conversation.setId(java.util.UUID.randomUUID());

        doNothing().when(conversationRepository).persist(conversation);
        when(conversationRepository.findById(conversation.getId())).thenReturn(conversation);
        when(messageRepository.findByConversationId(any(UUID.class))).thenReturn(List.of(
                new Message.MessageBuilder().message("test").conversation(conversation).build()));
        when(systemPromptRepository.findByConversationId(any(UUID.class))).thenReturn(new SystemPrompt("test"));

        customChatMemoryStore.updateMessages(conversation.getId().toString(), List.of(new UserMessage("test,test")));
        verify(messageRepository, times(1)).persist(any(Message.class));
        verify(systemPromptRepository, times(0)).persist(any(SystemPrompt.class));

        System.out.println(customChatMemoryStore.getMessages(conversation.getId().toString()));
        List<ChatMessage> messages = customChatMemoryStore.getMessages(conversation.getId().toString());

        assertEquals(2, messages.size());
        assertEquals(SystemMessage.class, messages.get(0).getClass());
    }

}