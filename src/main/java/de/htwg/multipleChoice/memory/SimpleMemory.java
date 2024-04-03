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
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
public class SimpleMemory implements ChatMemoryStore{

    ConversationRepository conversationRepository = new ConversationRepository();
    SimpleChatMessageRepository messageRepository = new SimpleChatMessageRepository();


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

    public void updateMessages(Object o, List<ChatMessage> list) {
        System.out.println(list.toString());
        ChatMessage lastMessage = list.get(list.size() - 1);
        SimpleChatMessage message;
        UUID memoryIdUUID = o instanceof UUID ? (UUID) o : UUID.fromString((String) o);
        Conversation conversation = conversationRepository.findById(memoryIdUUID);

        if (lastMessage instanceof UserMessage) {
            message = new SimpleChatMessage(conversation, ((UserMessage) lastMessage).contents().toString(), "user");
        } else if (lastMessage instanceof AiMessage) {
            message = new SimpleChatMessage(conversation, ((AiMessage) lastMessage).text(), "assistant");
        } else {
            message = new SimpleChatMessage(conversation, ((SystemMessage) lastMessage).text(), "system");
        }

        messageRepository.persist(message);

    }

    public void deleteMessages(Object o) {
        UUID memoryIdUUID = o instanceof UUID ? (UUID) o : UUID.fromString((String) o);
        messageRepository.deleteByConversationId(memoryIdUUID);
    }
}
