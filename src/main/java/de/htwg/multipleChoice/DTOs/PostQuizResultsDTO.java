package de.htwg.multipleChoice.DTOs;


import java.util.List;

public class PostQuizResultsDTO {

    String conversationId;
    String quizId;
    List<String> results;
    String studentId;

    public PostQuizResultsDTO(String conversationId, String quizId, List<String> results) {
        this.conversationId = conversationId;
        this.quizId = quizId;
        this.results = results;
    }

    public PostQuizResultsDTO(String conversationId, String quizId, List<String> results, String studentId) {
        this.conversationId = conversationId;
        this.quizId = quizId;
        this.results = results;
        this.studentId = studentId;
    }

    public PostQuizResultsDTO() {

    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
