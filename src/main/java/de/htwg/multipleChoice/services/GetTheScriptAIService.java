package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GetTheScriptDTO;
import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.memory.SimpleMemoryProvider;
import de.htwg.multipleChoice.tools.GetLectureScript;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(modelName = "get-the-script", tools = {GetLectureScript.class}, chatMemoryProviderSupplier = CustomMemoryProvider.class)
public interface GetTheScriptAIService {

    @SystemMessage("The User will provide you with a scriptId or a script name. " +
            "You get the specific script by using the tools provided to you. " +
            "If the user does not provide a scriptId or scriptName, " +
            "you will ask for a scriptId or scriptName in the errorText " +
            "and return success = false")
    GetTheScriptDTO getTheScript(@UserMessage String userInput, @MemoryId UUID memoryId);
}
