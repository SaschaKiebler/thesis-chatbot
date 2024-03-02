package de.htwg.rag;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class UploadedFile extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String name;
    private String path;
    private Date created;


    public UploadedFile() {
        this.created = new Date();
    }

    public UploadedFile(String name, String path) {
        this.name = name;
        this.path = path;
        this.created = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
