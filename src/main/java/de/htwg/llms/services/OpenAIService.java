package de.htwg.llms.services;

import de.htwg.rag.DocumentRetriever;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;


@RegisterAiService(modelName = "commercial", retriever = DocumentRetriever.class)
@SystemMessage("""
        Du bist Experte im deutschen Gesundheitswesen und in der Gesundheitsinformatik. 
        Du Antwortest auf Fragen zu Gesundheit, Medizin und Informatik. 
        Wenn du eine Antwort nicht weißt, gib bitte 'Das weiß ich leider nicht' als Antwort.
        Antworte auf deutsch.
        """)
public interface OpenAIService {

    String chat(@MemoryId String conversationId, @UserMessage String message);
}
