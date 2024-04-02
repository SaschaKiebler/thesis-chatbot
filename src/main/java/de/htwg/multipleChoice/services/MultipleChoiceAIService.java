package de.htwg.multipleChoice.services;

import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import de.htwg.multipleChoice.tools.GetLectureScript;
import de.htwg.multipleChoice.tools.QuizTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.util.UUID;

/**
 * TODO: Add a fallback Method to make it more robust
 *
 * This AiService provides a method to interact with a LLM and generate a multiple choice quiz from a lecturescript.
 */
@RegisterAiService(modelName = "multiple-choice", tools = {GetLectureScript.class, QuizTools.class}, chatMemoryProviderSupplier = CustomMemoryProvider.class)
public interface MultipleChoiceAIService {

    @SystemMessage(
            """
                    Als Quizmaster wirst du mit einer Skript-ID versorgt, die der Nutzer angibt. Deine Aufgabe ist es, zuerst das spezifizierte Skript mit dem geeigneten Tool abzurufen. Sobald du das Skript erhalten hast, erstelle ein Quiz bestehend immer aus sieben Fragen. Jede Frage soll drei Antwortmöglichkeiten bieten, um dem Nutzer beim Lernen des Skriptinhalts zu helfen. Achte darauf, dass die Fragen effektiv zum Lernprozess beitragen und auf Informationen aus dem Skript basieren. Nachdem du das Quiz erstellt hast, präsentiere die Fragen in einem angemessenen und verständlichen Format, sodass der Nutzer daraus effektiv lernen kann. Beachte, dass jede Antwort korrekt und klar sein sollte.
                    """
    )
    @Timeout(value = 200000)
    @Fallback(fallbackMethod = "fallback")
    MultipleChoiceAnswerFormat getQuestion(@UserMessage String Text, @MemoryId String memoryId);

    default MultipleChoiceAnswerFormat fallback(String scriptId, String memoryId) {
        MCQuizRepository mcQuizRepository = new MCQuizRepository();
        try{
            System.out.println(scriptId);
        String quizId = mcQuizRepository.findByScriptId(UUID.fromString(scriptId)).getId().toString();
        if (quizId == null || quizId.isEmpty()) {
            return new MultipleChoiceAnswerFormat("Es ist etwas schiefgelaufen beim erzeugen des Quizzes. Bitte versuche es erneut.");

        }
        return new MultipleChoiceAnswerFormat("Es gab einen kleinen Fehler... Hier ist ein Ersatzquiz", quizId);

        } catch (Exception e) {
            e.printStackTrace();
            return new MultipleChoiceAnswerFormat("Es ist etwas schiefgelaufen beim erzeugen des Quizzes🫣. Bitte versuche es erneut.");
        }

    }
}
