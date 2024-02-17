package de.htwg.chat.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class CustomMemoryProvider implements ChatMemoryProvider {

    private final CustomChatMemoryStore store = new CustomChatMemoryStore();
    @Override
    public ChatMemory get(Object memoryId) {
        return MessageWindowChatMemory.builder()
                .chatMemoryStore(store)
                .maxMessages(10)
                .id(memoryId)
                .build();
    }
}
