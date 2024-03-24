package de.htwg.multipleChoice;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import dev.langchain4j.model.output.structured.Description;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class QuizTools {



    @Tool("Sends the multiple choice Question to the user. Make sure to send use the right format.")
    public void sendQuestionToUser(@P("the question for the user") String question,
                                   @P("the first possible answer") String answer1,
                                   @P("the second possible answer") String answer2,
                                   @P("the third possible answer") String answer3,
                                   @P("the correct answer") String correctAnswer,
                                   @ToolMemoryId String memoryId) {
        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(new PossibleAnswer(answer1, answer1.equals(correctAnswer)));
        possibleAnswers.add(new PossibleAnswer(answer2, answer2.equals(correctAnswer)));
        possibleAnswers.add(new PossibleAnswer(answer3, answer3.equals(correctAnswer)));
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(question, possibleAnswers);

        System.out.println(multipleChoiceQuestion);
    }


    public static class AnswerOption {
        @Description("A List of Strings with the possible Answers for the question")
        List<String> answer;
        @Description("a String with the correct answer for the question")
        String correct;

        public AnswerOption() {
        }

        public AnswerOption(List<String> answer, String correct) {
            this.answer = answer;
            this.correct = correct;
        }

        public List<String> answer() {
            return answer;
        }

        public String correct() {
            return correct;
        }

        @Override
            public String toString() {
                return "PossibleAnswer{" +
                        "answer='" + answer + '\'' +
                        ", correct=" + correct +
                        '}';
            }
        }
}
