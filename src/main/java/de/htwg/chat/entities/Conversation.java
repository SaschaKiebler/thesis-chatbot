package de.htwg.chat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private Date date;

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

}
