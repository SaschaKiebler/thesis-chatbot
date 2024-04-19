package de.htwg.multipleChoice.DTOs;

import java.util.Map;

public class LectureResultDTO {

    Map<String, Float> results;

    public LectureResultDTO(Map<String, Float> results) {
        this.results = results;
    }

    public Map<String, Float> getResults() {
        return results;
    }

    public void setResults(Map<String, Float> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "LectureResultDTO{" +
                "results=" + results +
                '}';
    }
}
