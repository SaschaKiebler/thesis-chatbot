package de.htwg.rag.dataTools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * This interface represents the service to extract text from a website.
 */
@SystemMessage("""
        Extrahiere den Text aus der gegebenen Webseite.
        """)
public interface WebsiteTextExtractorService {

        /**
         * This method is called when a user sends a message to the AI and the classifier
         * decides there's a url in the request.
         * It returns the extracted text.
         * @param website The website to extract text from as a String.
         * @return The extracted text.
         */
        String extractText(@UserMessage String website);
}
