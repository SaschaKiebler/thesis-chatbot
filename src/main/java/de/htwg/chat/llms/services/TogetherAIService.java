package de.htwg.chat.llms.services;

import de.htwg.rag.retriever.AdvancedRetrievalAugmentor;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * This interface represents the AI service for the opensource configuration and uses the AdvancedRetrievalAugmentor for retrieval augmentation
 */
@RegisterAiService(modelName = "opensource", retrievalAugmentor = AdvancedRetrievalAugmentor.class)
public interface TogetherAIService {
    /**
     * This method is called when a user sends a message to the AI.
     * It returns the response of the AI.
     * @param conversationId The id of the conversation.
     * @param message The message of the user.
     * @param prompt The system message for the LLM.
     * @return The response of the AI.
     */
    @SystemMessage("""
            {{prompt}}
            """)
    String chat(@MemoryId String conversationId, @UserMessage String message, @V("prompt") String prompt);
}
