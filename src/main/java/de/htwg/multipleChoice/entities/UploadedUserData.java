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


}
