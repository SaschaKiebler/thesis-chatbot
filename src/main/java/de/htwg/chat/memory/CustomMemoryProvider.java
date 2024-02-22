package de.htwg.chat.memory;

import de.htwg.llms.Modeltype;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.util.function.Supplier;

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
