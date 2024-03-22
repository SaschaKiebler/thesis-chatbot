package de.htwg.rag.dataTools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


/**
 * This interface represents the text summarizer service.
 */
@SystemMessage("""
        Bereinige den Text um unnötige oder irreführende Wörter und Zeichen. Gebe ihn im Anschluss in Stichpunkten aus.
        """)
public interface TextSummarizerService {

    /**
     * This method is called when a user sends a message to the AI.
     * It returns the summarized text.
     * @param text The text to summarize.
     * @return The summarized text.
     */
    String summarize(@UserMessage String text);
}
