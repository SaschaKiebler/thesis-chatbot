package de.htwg.rag.ingestor;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

/**
 * This class represents an uploaded file.
 */
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

    public static UploadedFileBuilder builder() {
        return new UploadedFileBuilder();
    }

    public static class UploadedFileBuilder {
        private UUID id;
        private String name;
        private String path;
        private Date created;

        public UploadedFileBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public UploadedFileBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UploadedFileBuilder path(String path) {
            this.path = path;
            return this;
        }

        public UploadedFileBuilder created(Date created) {
            this.created = created;
            return this;
        }

        public UploadedFile build() {
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.id = this.id;
            uploadedFile.name = this.name;
            uploadedFile.path = this.path;
            uploadedFile.created = this.created;
            return uploadedFile;
        }
    }
}
