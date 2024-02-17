package de.htwg.chat.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomMemoryProvider implements ChatMemoryProvider {

    @Inject
    CustomChatMemoryStore store;
    @Override
    public ChatMemory get(Object memoryId) {
        return MessageWindowChatMemory.builder()
                .chatMemoryStore(store)
                .maxMessages(10)
                .id(memoryId)
                .build();
    }
}
