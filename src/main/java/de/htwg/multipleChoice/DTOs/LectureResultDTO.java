package de.htwg.multipleChoice.DTOs;

import java.util.Map;

public class LectureResultDTO {

    String lectureName;
    float result;

    public LectureResultDTO(String lectureName, float result) {
        this.lectureName = lectureName;
        this.result = result;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LectureResultDTO{" +
                "lectureName='" + lectureName + '\'' +
                ", result=" + result +
                '}';
    }
}
