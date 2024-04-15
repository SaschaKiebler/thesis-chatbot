package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.InMemoryProvider;
import de.htwg.multipleChoice.tools.WebDataTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(modelName = "web-scraper", chatMemoryProviderSupplier = InMemoryProvider.class, tools = {WebDataTools.class})
public interface WebScraperService {

    @SystemMessage("Retrieve the text from the specified website by utilizing the designated tools " +
            "available and give the user the full text back")
    String scrapeURL(@UserMessage String input, @MemoryId UUID id);
}
