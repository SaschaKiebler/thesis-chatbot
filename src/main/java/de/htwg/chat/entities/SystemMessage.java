package de.htwg.chat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class SystemMessage {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String message;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

    public SystemMessage() {
    }

    public SystemMessage(String message) {
        this.message = message;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }


}
