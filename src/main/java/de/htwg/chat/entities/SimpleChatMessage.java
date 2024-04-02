package de.htwg.chat.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class SimpleChatMessage {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(length = 30000)
    private String text;
    private String role;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

    public SimpleChatMessage() {

    }

    public SimpleChatMessage(Conversation conversation,String text, String role ) {
        this.text = text;
        this.role = role;
        this.conversation = conversation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
