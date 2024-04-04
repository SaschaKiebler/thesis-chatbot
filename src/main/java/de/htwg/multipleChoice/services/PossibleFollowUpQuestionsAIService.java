package de.htwg.multipleChoice.services;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;

@RegisterAiService(modelName = "possible-questions", chatMemoryProviderSupplier = RegisterAiService.NoChatMemoryProviderSupplier.class)
public interface PossibleFollowUpQuestionsAIService {

    @SystemMessage("The User will provide you with a message. " +
            "You will generate a list of 4 questions that could be asked next by the user. " +
            "After generating the questions, you will answer the user in the right format. " )
    List<String> possibleQuestionsChat(@UserMessage String userInput);
}
