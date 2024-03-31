package de.htwg.multipleChoice.entities;

import de.htwg.multipleChoice.entities.PossibleAnswer;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String question;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private List<PossibleAnswer> possibleAnswers = new ArrayList<>();

    public MultipleChoiceQuestion() {
    }

    public MultipleChoiceQuestion(String question, List<PossibleAnswer> answers) {
        this.question = question;
        this.possibleAnswers = answers;
    }

    public MultipleChoiceQuestion(String question, List<PossibleAnswer> answers, UUID id) {
        this.question = question;
        this.possibleAnswers = answers;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public List<PossibleAnswer> getAnswers() {
        return possibleAnswers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<PossibleAnswer> answers) {
        this.possibleAnswers = answers;
    }

    public void addAnswer(PossibleAnswer answer) {
        possibleAnswers.add(answer);
    }

    public void removeAnswer(PossibleAnswer answer) {
        possibleAnswers.remove(answer);
    }

    public void removeAllAnswers() {
        possibleAnswers.clear();
    }

    public PossibleAnswer getCorrectAnswer() {
        for (PossibleAnswer possibleAnswer : possibleAnswers) {
            if (possibleAnswer.getCorrect()) {
                return possibleAnswer;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                '}';
    }
}
