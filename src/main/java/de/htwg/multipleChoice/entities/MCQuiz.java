package de.htwg.multipleChoice.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a multiple choice quiz.
 */
@Entity
public class MCQuiz {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "quiz_id")
    private List<MultipleChoiceQuestion> questions = new ArrayList<>();


    public MCQuiz(UUID id) {
        this.id = id;
    }

    public MCQuiz(UUID id, List<MultipleChoiceQuestion> questions) {
        this.id = id;
        this.questions = questions;
    }

    public MCQuiz() {

    }

    /**
     * Get the ID of the quiz.
     *
     * @return         the UUID representing the ID
     */
    public UUID getId() {
        return id;
    }

    public List<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MultipleChoiceQuestion> questions) {
        this.questions = questions;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Add a question to the quiz.
     *
     */
    public void addQuestion(MultipleChoiceQuestion question) {
        questions.add(question);
    }

    public List<UUID> getCorrectAnswers() {
        List<UUID> correctAnswers = new ArrayList<>();
        for (MultipleChoiceQuestion question : questions) {
            if (question.getCorrectAnswer() != null) {
                correctAnswers.add(question.getCorrectAnswer().getId());
            }
        }
        return correctAnswers;
    }

    @Override
    public String toString() {
        return "MCQuiz{" +
                "id=" + id +
                ", questions=" + questions +
                '}';
    }

}
