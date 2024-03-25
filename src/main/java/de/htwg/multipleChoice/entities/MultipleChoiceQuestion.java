package de.htwg.multipleChoice.entities;

import de.htwg.multipleChoice.entities.PossibleAnswer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String question;

    @OneToMany(targetEntity = PossibleAnswer.class)
    private List<PossibleAnswer> possibleAnswers;

    public MultipleChoiceQuestion() {
    }

    public MultipleChoiceQuestion(String question, List<PossibleAnswer> answers) {
        this.question = question;
        this.possibleAnswers = answers;
    }

    public MultipleChoiceQuestion(String question, List<PossibleAnswer> answers, String id) {
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


    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                '}';
    }
}
