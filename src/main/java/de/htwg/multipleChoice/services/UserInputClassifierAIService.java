package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.tools.RequestType;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "input-classifier", chatMemoryProviderSupplier = RegisterAiService.NoChatMemoryProviderSupplier.class)
public interface UserInputClassifierAIService {


    @SystemMessage("Classify the user's input. If the User asks a normal question or just want to talk, classify it with NO_DATA." +
            "If the User provides a URL, classify it with URL. If the User provides a text for a quiz, classify it with TEXT. " +
            "If the User wants you to create another Quiz from the same Input classify it as SAME_TEXT")
    RequestType classify(String input);
}
