package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.memory.SimpleMemoryProvider;
import de.htwg.rag.retriever.AdvancedRetrievalAugmentor;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@RegisterAiService(modelName = "result-service", chatMemoryProviderSupplier = SimpleMemoryProvider.class, retrievalAugmentor = AdvancedRetrievalAugmentor.class)
public interface QuizResultAIService {
    @SystemMessage("Du bist Experte und Tutor. Du führst ein Gespräch mit dem Nutzer " +
            "über seine Ergebnisse im Quiz, die ihm bereits vorliegen, und versuchst aufgetretene Fragen zu beantworten." +
            "Antworte kurz und mit den wichtigsten Punkten.")

    String resultChat(@UserMessage String userInput, @MemoryId UUID memoryId);
}
