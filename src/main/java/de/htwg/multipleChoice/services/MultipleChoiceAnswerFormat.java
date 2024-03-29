package de.htwg.multipleChoice.services;


import dev.langchain4j.model.output.structured.Description;

public class MultipleChoiceAnswerFormat {

    @Description("The answer to the Users question")
    String Answer;
    @Description("The id of the generated Quiz")
    String quizId;

    public MultipleChoiceAnswerFormat() {

    }

    public MultipleChoiceAnswerFormat(String answer) {
        Answer = answer;
    }


    public MultipleChoiceAnswerFormat(String answer, String quizId) {
        Answer = answer;
        this.quizId = quizId;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getQuizId() {
        return quizId;
    }

    @Override
    public String toString() {
        // in JSON format
        return "Answer: " + Answer + ", quizId: " + quizId;
    }
}
