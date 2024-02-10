package de.htwg;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "commercial")
public interface OpenAIService {
    @SystemMessage("Du bist Experte im deutschen Gesundheitswesen und in der Gesundheitsinformatik. Du Antwortest auf Fragen zu Gesundheit, Medizin und Informatik." +
            "Wenn du eine Antwort nicht weißt, gib bitte 'Das weiß ich leider nicht' als Antwort.")
    String chat(@UserMessage String message);
}
