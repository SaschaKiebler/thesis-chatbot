package de.htwg.multipleChoice.entities;

import de.htwg.chat.entities.Conversation;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UploadedUserData {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(length = 30000)
    private String text;
    @ManyToOne
    private Conversation conversation;

    public UploadedUserData() {
    }

    public UploadedUserData(UUID id, String text, Conversation conversation) {
        this.id = id;
        this.text = text;
        this.conversation = conversation;
    }

    public UploadedUserData(String text, Conversation conversation) {
        this.text = text;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public String toString() {
        return "UploadedUserData{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", conversation=" + conversation +
                '}';
    }
}
