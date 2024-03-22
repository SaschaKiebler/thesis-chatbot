package de.htwg.rag.dataTools;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;

/**
 * This class is used to extract text from a website served as a String with an LLM.
 */
@ApplicationScoped
public class WebsiteTextExtractor {

    /**
     * This method is used to extract text from a website with an LLM.
     * It returns the extracted text.
     *
     * @param website the website to extract text from
     * @return the extracted text
     */
    public String extractText(String website) {
        if (website == null || website.isEmpty()) {
            throw new IllegalArgumentException("Text is empty or null");
        }

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_APIKEY"))
                .modelName("gpt-3.5-turbo")
                .timeout(Duration.ofMinutes(5))
                .logRequests(true)
                .logResponses(true)
                .build();
        WebsiteTextExtractorService textExtractor = AiServices.create(WebsiteTextExtractorService.class, model);

        return textExtractor.extractText(website);

    }
}
