package de.htwg.chat.entities;

import jakarta.persistence.*;

import java.util.Date;

import java.util.UUID;

/**
 * Represents the users input message.
 * The date is set to the current date and time when the message is created.
 * the model should be set with the Modeltype Enum values.
 * The id is a UUID and will be autogenerated.
 * It uses the builder pattern to create a new message.
 *
 */
@Entity
public class Message {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(length = 3000)
    private String message;
    private Date date;
    private String model;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

    public Message() {
    }

    private Message(MessageBuilder messageBuilder){
        this.message = messageBuilder.message;
        this.date = messageBuilder.date;
        this.model = messageBuilder.model;
        this.conversation = messageBuilder.conversation;
        this.id = messageBuilder.id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public static MessageBuilder builder(){
        return new MessageBuilder();
    }

    public static class MessageBuilder{
        private String message;

        private UUID id;
        private Date date;
        private String model;
        private Conversation conversation;

        public MessageBuilder() {
            this.date = new Date();
        }


        public MessageBuilder id(UUID id){
            this.id = id;
            return this;
        }

        public MessageBuilder message(String message){
            this.message = message;
            return this;
        }

        public MessageBuilder date(Date date){
            this.date = date;
            return this;
        }

        public MessageBuilder model(String model){
            this.model = model;
            return this;
        }

        public MessageBuilder conversation(Conversation conversation){
            this.conversation = conversation;
            return this;
        }

        public Message build(){
            return new Message(this);
        }
    }


}
