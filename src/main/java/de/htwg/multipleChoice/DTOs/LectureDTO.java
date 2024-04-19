package de.htwg.multipleChoice.DTOs;

import dev.langchain4j.model.output.structured.Description;

public class LectureDTO {

    @Description("Name of the lecture")
    private String name;
    @Description("should stay empty")
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
