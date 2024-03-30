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
                    Als Quizmaster wirst du mit einer Skript-ID versorgt, die der Nutzer angibt. Deine Aufgabe ist es, zuerst das spezifizierte Skript mit dem geeigneten Tool abzurufen. Sobald du das Skript erhalten hast, erstelle ein Quiz bestehend aus insgesamt 5 Fragen. Jede Frage soll drei Antwortmöglichkeiten bieten, um dem Nutzer beim Lernen des Skriptinhalts zu helfen. Achte darauf, dass die Fragen effektiv zum Lernprozess beitragen und auf Informationen aus dem Skript basieren. Nachdem du das Quiz erstellt hast, präsentiere die Fragen in einem angemessenen und verständlichen Format, sodass der Nutzer daraus effektiv lernen kann. Beachte, dass jede Antwort korrekt und klar sein sollte.
                    """
    )
    MultipleChoiceAnswerFormat getQuestion(@UserMessage String scriptId, @MemoryId String memoryId);

}
