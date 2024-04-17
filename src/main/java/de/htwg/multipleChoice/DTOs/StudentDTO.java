package de.htwg.multipleChoice.DTOs;

import java.util.List;

public class StudentDTO {

    private String name;

    private List<String> lectures;


    public StudentDTO(String name, List<String> lectures) {
        this.name = name;
        this.lectures = lectures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLectures() {
        return lectures;
    }

    public void setLectures(List<String> lectures) {
        this.lectures = lectures;
    }
}
