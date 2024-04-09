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

    @SystemMessage("Get the text of the website the user provided the url for by using the provided tools")
    String scrapeURL(@UserMessage String input, @MemoryId UUID id);
}
