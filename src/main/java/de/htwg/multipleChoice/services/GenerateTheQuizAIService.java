package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.InMemoryProvider;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GenerateTheQuizDTO;
import de.htwg.multipleChoice.tools.QuizTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(modelName = "generate-the-quiz", tools = {QuizTools.class}, chatMemoryProviderSupplier = InMemoryProvider.class)
public interface GenerateTheQuizAIService {

    @SystemMessage("The User will provide you with a text. " +
            "You will generate a difficult quiz from this text with seven questions that each have 3 possible answers." +
            "The correct answer should be one of the possible answers. " +
            "After generating the quiz, you will answer the user in the right format. " )
    GenerateTheQuizDTO generateTheQuiz(@UserMessage String Text, @MemoryId UUID memoryId);
}
