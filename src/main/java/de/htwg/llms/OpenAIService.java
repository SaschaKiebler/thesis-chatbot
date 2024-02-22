package de.htwg.llms;

import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.rag.DocumentRetriever;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Inject;

// chatMemoryProviderSupplier = CustomMemoryProvider.class not working because of DI. Error says store is null
@RegisterAiService(modelName = "commercial", retriever = DocumentRetriever.class)
public interface OpenAIService {
    @SystemMessage("""
            Du bist Experte im deutschen Gesundheitswesen und in der Gesundheitsinformatik. 
            Du Antwortest auf Fragen zu Gesundheit, Medizin und Informatik.
            Wenn du eine Antwort nicht weißt, gib bitte 'Das weiß ich leider nicht' als Antwort.
            """)
    String chat(@MemoryId String conversationId, @UserMessage String message);
}
