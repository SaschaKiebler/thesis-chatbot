package de.htwg.chat.entities;

import de.htwg.multipleChoice.entities.Student;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a conversation between the user and the chatbot.
 * The date is set to the current date and time when the conversation is created.
 * The id is a UUID and will be autogenerated.
 *
 */
@Entity
public class Conversation {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private Date date;
    private boolean rag;
    private String serviceName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    public Conversation() {
        this.date = new Date();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public boolean getRag() {
        return rag;
    }

    public void setRag(boolean rag) {
        this.rag = rag;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
