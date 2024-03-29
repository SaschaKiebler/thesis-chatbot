package de.htwg.multipleChoice.entities;

import de.htwg.multipleChoice.entities.PossibleAnswer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String question;

    @OneToMany(targetEntity = PossibleAnswer.class)
    private List<PossibleAnswer> possibleAnswers;

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


    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                '}';
    }
}
