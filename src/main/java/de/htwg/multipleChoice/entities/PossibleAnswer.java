package de.htwg.multipleChoice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class PossibleAnswer {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(length = 2000)
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

    public boolean getCorrect() {
        return correct;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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
