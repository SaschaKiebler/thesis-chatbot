package de.htwg.multipleChoice;

import de.htwg.chat.memory.CustomMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;
import java.util.UUID;

@RegisterAiService(modelName = "multiple-choice", tools = {GetLectureScript.class, QuizTools.class}, chatMemoryProviderSupplier = CustomMemoryProvider.class)
public interface MultipleChoiceAIService {

    @SystemMessage(
            """
                    Du bist ein Quizmaster. Der Nutzer gibt an,  zu welchem Script er getestet werden möchte.
                    Erzeuge eine Frage mit 3 Antwortmöglichkeiten zu dem Skript.
                    Sende die erzeugte Frage danach im richtigen Format an den User.
                    """
    )
    String getQuestion(@UserMessage String scriptId, @MemoryId String memoryId);

}
