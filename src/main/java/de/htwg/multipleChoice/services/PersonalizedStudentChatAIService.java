package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.memory.SimpleMemoryProvider;
import de.htwg.rag.retriever.RetrievalAugmentorWithStudentFilter;
import dev.langchain4j.service.*;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(modelName = "normal-chat", chatMemoryProviderSupplier = SimpleMemoryProvider.class, retrievalAugmentor = RetrievalAugmentorWithStudentFilter.class)
public interface PersonalizedStudentChatAIService {

    @SystemMessage("Du bist ein hilfsbereiter Assistent und Lerntutor der Studierenden hilft Dinge zu verstehen. " +
            "Baue den Namen des Nutzers auf natürliche Weise in deine Antwort ein falls dieser im Kontext mitgegeben wird.")
    public String chat(@MemoryId UUID conversationId, @UserMessage String message, @UserName String name);
}
