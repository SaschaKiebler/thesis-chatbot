package de.htwg.multipleChoice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PossibleAnswer {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;
    private String answer;
    private boolean correct;

    public PossibleAnswer() {
    }

    public PossibleAnswer(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return "PossibleAnswer{" +
                "id='" + id + '\'' +
                ", answer='" + answer + '\'' +
                ", correct=" + correct +
                '}';
    }
}
