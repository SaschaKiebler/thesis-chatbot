package de.htwg.chat.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Supplier;

@ApplicationScoped
public class CustomMemoryProvider implements Supplier<ChatMemoryProvider> {

    @Inject
    CustomChatMemoryStore store;



    @Override
    public ChatMemoryProvider get() {
        return this::createChatMemory;
    }

    private ChatMemory createChatMemory(Object memoryId) {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .id(memoryId)
                .chatMemoryStore(store)
                .build();
    }
}
