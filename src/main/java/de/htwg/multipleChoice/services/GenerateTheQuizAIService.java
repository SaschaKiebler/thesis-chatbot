package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GenerateTheQuizDTO;
import de.htwg.multipleChoice.memory.SimpleMemoryProvider;
import de.htwg.multipleChoice.tools.QuizTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@RegisterAiService(modelName = "generate-the-quiz", tools = {QuizTools.class}, chatMemoryProviderSupplier = CustomMemoryProvider.class)
public interface GenerateTheQuizAIService {

    @SystemMessage("The User will provide you with a text. " +
            "You will generate a quiz from this text with seven questions that each have 3 possible answers." +
            "The answer should be one of the possible answers. " +
            "After generating the quiz, you will answer the user in the right format. " )
    GenerateTheQuizDTO generateTheQuiz(@UserMessage String Text, @MemoryId UUID memoryId);
}
