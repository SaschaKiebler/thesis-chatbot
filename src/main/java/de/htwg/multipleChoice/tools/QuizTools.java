package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.entities.MultipleChoiceQuestion;
import de.htwg.multipleChoice.entities.PossibleAnswer;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a method as a langchain4j Tool to generate multiple choice questions for a quiz.
 */
@ApplicationScoped
public class QuizTools {


    /**
     * This method is a tool for an AiService to send a multiple choice question to the user.
     * @param question The question for the user.
     * @param answer1 The first possible answer.
     * @param answer2 The second possible answer.
     * @param answer3 The third possible answer.
     * @param correctAnswer The number of the correct answer.
     */
    @Tool("Sends the multiple choice Question to the user. Make sure to send use the right format. Also be sure that the correct answer is one of the three possible answers. give the answer and questions in german.")
    public void sendQuestionToUser(@P("the question for the user") String question,
                                   @P("the first possible answer") String answer1,
                                   @P("the second possible answer") String answer2,
                                   @P("the third possible answer") String answer3,
                                   @P("the number of the correct answer") int correctAnswer) {
        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(new PossibleAnswer(answer1, correctAnswer == 1));
        possibleAnswers.add(new PossibleAnswer(answer2, correctAnswer == 2));
        possibleAnswers.add(new PossibleAnswer(answer3, correctAnswer == 3));
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(question, possibleAnswers);

        System.out.println(multipleChoiceQuestion);
    }


}
