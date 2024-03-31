package de.htwg.multipleChoice.DTOs;

public class LectureDTO {

    private String name;
    private String description;

    public LectureDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LectureDTO() { }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
