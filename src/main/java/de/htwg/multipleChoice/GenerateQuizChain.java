package de.htwg.multipleChoice;

import de.htwg.multipleChoice.DTOs.serviceDTOs.GenerateTheQuizDTO;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GetTheScriptDTO;
import de.htwg.multipleChoice.memory.SimpleMemory;
import de.htwg.multipleChoice.services.GenerateTheQuizAIService;
import de.htwg.multipleChoice.services.GetTheScriptAIService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GenerateQuizChain {

    // This class will chain multiple AIServices with specifc tools together and make
    // the quiz workflow more robust. Also it helps adding stuff like a router between
    // the Experts. The Advantage of this approach is that it will be easier to add
    // different models with different strengths to the conversation and not distract the
    // models from doing one thing right.
    @Inject
    GetTheScriptAIService getTheScriptAIService;
    @Inject
    GenerateTheQuizAIService generateTheQuizAIService;

    public String startTheChain(String userInput, UUID conversationId) {


        // First step in the chain is to get the script. be it by id or just the name, or the most similar name
        GetTheScriptDTO getTheScriptDTO = getTheScriptAIService.getTheScript(userInput, conversationId);

        // Second step is to generate a new quiz and then add the questions to the quiz.
        GenerateTheQuizDTO generateTheQuizDTO = generateTheQuizAIService.generateTheQuiz(getTheScriptDTO.getText(), conversationId);

        // After that it should be sent to the user
        return generateTheQuizDTO.getQuizId();

        // Third step is to wait for the user to answer the questions and send the results back

        // Fourth step is to give the user examples what he could ask or talk about with the results

        // Fifth step is to hold a conversation with the user about the results while always having the context of the lecture

    }



}
