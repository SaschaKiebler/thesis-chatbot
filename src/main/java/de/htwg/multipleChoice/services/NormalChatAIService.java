package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.memory.SimpleMemoryProvider;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(modelName = "normal-chat", chatMemoryProviderSupplier = SimpleMemoryProvider.class)
public interface NormalChatAIService {

    @SystemMessage("Du bist ein hilfsbereiter Assistent und Lerntutor der Studierenden hilft Dinge zu verstehen.")
    public String chat(@MemoryId UUID conversationId, @UserMessage String message);
}
