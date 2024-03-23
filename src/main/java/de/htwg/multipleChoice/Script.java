package de.htwg.multipleChoice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

/**
 * Represents a script for a lecture.
 * The id is a UUID and will be autogenerated.
 * It uses the builder pattern to create a new script.
 * It has a ManyToOne relationship with the Lecture class.
 *
 */
@Entity
public class Script {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String name;
    private String text;
    @ManyToOne(targetEntity = Lecture.class)
    private Lecture lecture;

    public Script() {
    }

    public Script(String name, String text, Lecture lecture) {
        this.name = name;
        this.text = text;
        this.lecture = lecture;
    }

    public Script(String name, String text, Lecture lecture, UUID id) {
        this.name = name;
        this.text = text;
        this.lecture = lecture;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public UUID getId() {
        return id;
    }

    public static ScriptBuilder builder() {
        return new ScriptBuilder();
    }

    public static class ScriptBuilder {
        private String name;
        private String text;
        private Lecture lecture;
        private UUID id;

        public ScriptBuilder() {

        }

        public ScriptBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ScriptBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ScriptBuilder lecture(Lecture lecture) {
            this.lecture = lecture;
            return this;
        }

        public ScriptBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public Script build() {
            return new Script(this.name, this.text, this.lecture, this.id);
        }
    }
}
