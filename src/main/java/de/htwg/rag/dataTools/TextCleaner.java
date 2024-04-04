package de.htwg.rag.dataTools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(chatMemoryProviderSupplier = RegisterAiService.NoChatMemoryProviderSupplier.class)
public interface TextCleaner {

    @UserMessage("""
            Bitte analysiere den folgenden Text und extrahiere die wesentlichen Informationen und Lerninhalte. Fokussiere dich dabei auf die Kernpunkte, Konzepte und Schlüsselinformationen. Ignoriere wiederkehrende Informationen wie den Namen des Professors und andere unwesentliche oder redundante Details. Gib eine zusammengefasste, klare und präzise Übersicht über die Hauptthemen und wichtigsten Punkte.
            
            Text:
            {text}
            """)
    String clean(@V("text") String text);
}
