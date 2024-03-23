package de.htwg.multipleChoice;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuizTools {

    @Tool("Sends the MultipleChoiceQuestion to the user.")
    public void sendQuestionToUser(MultipleChoiceQuestion question, String conversationId) {
        System.out.println(question.toString() + "   " + conversationId);
    }
}
