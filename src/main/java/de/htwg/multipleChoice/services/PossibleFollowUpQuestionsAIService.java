package de.htwg.multipleChoice.services;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;

@RegisterAiService(modelName = "possible-questions", chatMemoryProviderSupplier = RegisterAiService.NoChatMemoryProviderSupplier.class)
public interface PossibleFollowUpQuestionsAIService {

    /**
     * Defines the possible follow-up questions for the user based on the answer of the LLM
     * @param lastMessage the answer of the LLM
     * @return the possible follow-up questions as a list of strings
     */
    @SystemMessage("Du definierst drei mögliche Folgefragen des Nutzers basierend auf der letzten Antwort der KI. " +
            "Dabei ermittelst du relevante und wahrscheinliche Fragen, " +
            "die der Nutzer als Anschluss an die letzte Antwort der KI stellen könnte. " +
            "Ziel ist es, den Dialogfluss vorausschauend zu gestalten und proaktiv auf potenzielle " +
            "Informationsbedürfnisse des Nutzers einzugehen. Du gibst nur die Liste mit den möglichen Fragen zurück." )
    List<String> possibleQuestionsChat(@UserMessage String lastMessage);
}
