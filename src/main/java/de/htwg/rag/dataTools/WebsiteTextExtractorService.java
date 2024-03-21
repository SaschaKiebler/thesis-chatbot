package de.htwg.rag.dataTools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@SystemMessage("""
        Extrahiere den Text aus der gegebenen Webseite.      
        """)
public interface WebsiteTextExtractorService {

        String extractText(@UserMessage String website);
}
