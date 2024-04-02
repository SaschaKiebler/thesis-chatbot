package de.htwg.multipleChoice.memory;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.SimpleChatMessage;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.SimpleChatMessageRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class SimpleMemory implements ChatMemoryStore {

    @Inject
    ConversationRepository conversationRepository;
    @Inject
    SimpleChatMessageRepository messageRepository;


    @Override
    public List<ChatMessage> getMessages(Object o) {
        UUID memoryIdUUID = o instanceof UUID ? (UUID) o : UUID.fromString((String) o);
        Conversation conversation = conversationRepository.findById(memoryIdUUID);
        List<ChatMessage> messages = new ArrayList<>();
        if (conversation != null) {
            List<SimpleChatMessage> messageList = messageRepository.findByConversationId(memoryIdUUID);
            for (SimpleChatMessage message : messageList) {
                if (Objects.equals(message.getRole(), "system")) {
                    messages.add(new SystemMessage(message.getText()));
                } else if (Objects.equals(message.getRole(), "user")) {
                    messages.add(new UserMessage(message.getText()));
                } else if (Objects.equals(message.getRole(), "assistant")) {
                    messages.add(new AiMessage(message.getText()));
                }
            }
        }
        return messages;
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        List<SimpleChatMessage> messages = new ArrayList<>();
        UUID memoryIdUUID = o instanceof UUID ? (UUID) o : UUID.fromString((String) o);
        Conversation conversation = conversationRepository.findById(memoryIdUUID);

        for (ChatMessage message : list) {
            if(message instanceof UserMessage){
                messages.add(new SimpleChatMessage(conversation, "user", ((UserMessage) message).contents().toString()));
            } else if (message instanceof AiMessage) {
                messages.add(new SimpleChatMessage(conversation, "assistant", ((AiMessage) message).text()));
            } else if (message instanceof SystemMessage) {
                messages.add(new SimpleChatMessage(conversation, "system", ((SystemMessage) message).text()));
            }
        }

        messageRepository.saveAll(messages);


    }

    @Override
    public void deleteMessages(Object o) {
        UUID memoryIdUUID = o instanceof UUID ? (UUID) o : UUID.fromString((String) o);
        messageRepository.deleteByConversationId(memoryIdUUID);
    }
}
