package de.htwg.llms.services;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "opensource")

public interface TogetherAIServiceNoRAG {
        @SystemMessage("""
        {{prompt}}
            """)
        String chat(@MemoryId String conversationId, @UserMessage String message, @V("prompt") String prompt);

}
