package de.htwg.multipleChoice.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.chat.entities.Conversation;
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

    @ManyToOne(targetEntity = Lecture.class)
    private Lecture lecture;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

    @ManyToOne(targetEntity = Student.class)
    private Student student;

    @Column(name = "result")
    private float result;


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

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecture getLecture() {
        return this.lecture;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public float getResult() {
        return this.result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "MCQuiz{" +
                "id=" + id +
                ", questions=" + questions +
                '}';
    }


}
