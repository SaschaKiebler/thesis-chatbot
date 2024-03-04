package de.htwg.rag.dataTools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


@SystemMessage("""
        Bereinige den Text um unnötige oder irreführende Wörter und Zeichen. Gebe ihn im Anschluss in Stichpunkten aus.
        """)
public interface TextSummarizerService {


    String summarize(@UserMessage String text);
}
