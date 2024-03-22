package de.htwg.chat.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Represents the answer to the users input message.
 * The date is set to the current date and time when the answer is created.
 * The id is a UUID and will be autogenerated.
 * It uses the builder pattern to create a new answer.
 * It has a ManyToOne relationship with the Message class.
 *
 */
@Entity
public class Answer {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 30000)
    private String answer;
    private Date date;
    boolean preferred;

    String cause;

    @ManyToOne(targetEntity = Message.class)
    private Message message;


    public Answer() {
    }

    private Answer(AnswerBuilder answerBuilder){
        this.answer = answerBuilder.answer;
        this.date = answerBuilder.date;
        this.message = answerBuilder.message;
        this.id = answerBuilder.id;
        this.preferred = false;
        this.cause = "";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static AnswerBuilder builder(){
        return new AnswerBuilder();
    }
    public static class AnswerBuilder{
        private UUID id;
        private String answer;
        private Date date;
        private Message message;

        public AnswerBuilder() {
            this.date = new Date();
        }

        public AnswerBuilder id(UUID id){
            this.id = id;
            return this;
        }

        public AnswerBuilder answer(String answer){
            this.answer = answer;
            return this;
        }

        public AnswerBuilder date(Date date){
            this.date = date;
            return this;
        }

        public AnswerBuilder message(Message message){
            this.message = message;
            return this;
        }

        public Answer build(){
            return new Answer(this);
        }
    }
}
