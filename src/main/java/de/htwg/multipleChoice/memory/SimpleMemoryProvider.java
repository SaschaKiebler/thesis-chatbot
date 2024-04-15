package de.htwg.multipleChoice.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Supplier;

// Is not in Use bc the SimpleChatMemory cant be a bean. If it was, the CustomChatMemory doesnt work anymore
@ApplicationScoped
public class SimpleMemoryProvider implements Supplier<ChatMemoryProvider> {

    private final SimpleMemory store = new SimpleMemory();

    @Override
    public ChatMemoryProvider get() {
        return new ChatMemoryProvider() {

            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.builder()
                        .maxMessages(15)
                        .id(memoryId)
                        .chatMemoryStore(store)
                        .build();
            }
        };
    }
}
