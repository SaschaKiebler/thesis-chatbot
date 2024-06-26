package de.htwg.rag.dataTools;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to summarize a text with an LLM into a list.
 */
@ApplicationScoped
public class Summarizer {

    /**
     * This method is used to summarize a text with an LLM.
     * It returns the summarized text.
     *
     * @param text the text to summarize
     * @return the summarized text
     */
    public String summarize(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text is empty or null");
        }
        // Summarize the text and ingest it
        // Use of the @RegisterAiService annotation is not possible on TextSummarizer because no option to say chatMemory = null
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_APIKEY"))
                .modelName("gpt-3.5-turbo-0125")
                .timeout(Duration.ofMinutes(5))
                .logRequests(true)
                .logResponses(true)
                .build();
        TextSummarizerService summarizer = AiServices.create(TextSummarizerService.class, model);

        List<String> textList = Arrays.asList(text.split("\n"));

        // Create list of always 30 texts together
        List<List<String>> chunk = new ArrayList<>();
        for (int i = 0; i < textList.size(); i += 30) {
            chunk.add(textList.subList(i, Math.min(i + 30, textList.size())));
        }

        // Summarize the chunks and add them to the document
        StringBuilder summarizedText = new StringBuilder();
        for (List<String> strings : chunk) {
            summarizedText.append(summarizer.summarize(String.join("\n", strings)));
        }
        return summarizedText.toString();
    }
}
