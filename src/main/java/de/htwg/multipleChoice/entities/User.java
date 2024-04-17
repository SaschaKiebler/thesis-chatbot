package de.htwg.multipleChoice.entities;

import de.htwg.chat.entities.Conversation;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String username;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Lecture> lectures;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Conversation> conversations;


    public User(String username, List<Lecture> lectures) {
        this.username = username;
        this.lectures = lectures;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }
}
