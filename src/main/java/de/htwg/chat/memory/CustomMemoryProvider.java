package de.htwg.chat.memory;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Supplier;

/**
 * This class is a custom implementation of the ChatMemoryProvider interface.
 * It is used to provide a custom ChatMemoryProvider.
 * It uses the CustomChatMemoryStore to store the messages in a database.
 */
@ApplicationScoped
public class CustomMemoryProvider implements Supplier<ChatMemoryProvider> {

    @Inject
    CustomChatMemoryStore store;
    @Override
    public ChatMemoryProvider get() {
        if (this.store == null) {
            throw new IllegalStateException("ChatMemoryStore is not injected");
        }
        return memoryId -> MessageWindowChatMemory.builder()
                .maxMessages(10)
                .id(memoryId)
                .chatMemoryStore(store)
                .build();
    }
}
