package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.DTOs.LectureDTO;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;

@RegisterAiService(modelName = "lecture-classifier", chatMemoryProviderSupplier = RegisterAiService.NoChatMemoryProviderSupplier.class)
public interface LectureClassifierAIService {


     @SystemMessage("the user will provide you with a text and a list of the lectures he currently takes. " +
             "Decide to which lecture the text belongs to and return the accurate name of the lecture. ")
     @UserMessage("The Text: {{input}} \n\n\n The list of taken lectures: {{takenLectures}} \n\n\n make sure to answer with one of the lectures in the list")
    LectureDTO classifyInput(@V("input") String input, @V("takenLectures") List<String> takenLectures);
}
