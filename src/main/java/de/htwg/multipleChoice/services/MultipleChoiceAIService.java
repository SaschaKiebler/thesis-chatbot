package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.multipleChoice.tools.GetLectureScript;
import de.htwg.multipleChoice.tools.QuizTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * This AiService provides a method to interact with a LLM and generate a multiple choice quiz from a lecturescript.
 */
@RegisterAiService(modelName = "multiple-choice", tools = {GetLectureScript.class, QuizTools.class}, chatMemoryProviderSupplier = CustomMemoryProvider.class)
public interface MultipleChoiceAIService {

    @SystemMessage(
            """
                    Du bist ein Quizmaster. Der Nutzer gibt an,  zu welchem Script er getestet werden möchte.
                    Erzeuge insgesamt 5 Fragen mit 3 Antwortmöglichkeiten zu dem Skript.
                    Sende die erzeugte Frage danach im richtigen Format an den User.
                    Wenn der Nutzer kein Skript angibt, Frage ihn nach dem Skript und weise darauf hin, dass er eine 
                    normale Konversation nur im Q&A Modus führen kann.
                    """
    )
    String getQuestion(@UserMessage String scriptId, @MemoryId String memoryId);

}
