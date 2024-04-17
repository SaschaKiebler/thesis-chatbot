package de.htwg.multipleChoice.entities;

import de.htwg.chat.entities.Conversation;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Student {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String name;
    @OneToMany
    @JoinColumn(name = "student_id")
    private List<Conversation> conversations;
    @ManyToMany
    private List<Lecture> lectures;

    public Student() {

    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public Student(UUID id, String name, List<Conversation> conversations, List<Lecture> lectures) {
        this.id = id;
        this.name = name;
        this.conversations = conversations;
        this.lectures = lectures;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", conversations=" + conversations +
                ", lectures=" + lectures +
                '}';
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }


    public static class StudentBuilder {

        private UUID id;
        private String name;
        private List<Conversation> conversations;
        private List<Lecture> lectures;

        public StudentBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentBuilder conversations(List<Conversation> conversations) {
            this.conversations = conversations;
            return this;
        }

        public StudentBuilder lectures(List<Lecture> lectures) {
            this.lectures = lectures;
            return this;
        }

        public Student build() {
            return new Student(id, name, conversations, lectures);
        }
    }
}
