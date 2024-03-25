package de.htwg.chat.llms.services;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * This interface represents the commercial AI service without retrieval augmentation
 */
@RegisterAiService(modelName = "commercial")

public interface OpenAIServiceNoRAG {
    /**
     * This method is called when a user sends a message to the AI.
     * It returns the response of the AI.
     * @param conversationId The id of the conversation.
     * @param message The message of the user.
     * @param prompt The SystemMessage for the LLM.
     * @return The response of the AI.
     */
    @SystemMessage("""
        {{prompt}}
        """)
    String chat(@MemoryId String conversationId, @UserMessage String message, @V("prompt") String prompt);
}
