package de.htwg.llms.services;

import de.htwg.rag.DocumentRetriever;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "opensource", retriever = DocumentRetriever.class)
public interface TogetherAIService {
    @SystemMessage("""
            {{prompt}}
            """)
    String chat(@MemoryId String conversationId, @UserMessage String message, @V("prompt") String prompt);
}
