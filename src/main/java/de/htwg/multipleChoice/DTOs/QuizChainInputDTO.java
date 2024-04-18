package de.htwg.multipleChoice.DTOs;

import java.util.List;

/**
 * Represents the input for the method startQuizChain in {@link de.htwg.multipleChoice.resources.QuizChainResource}
 */
public class QuizChainInputDTO {

    private String conversationId;
    private String message;
    private String studentId;

    private List<String> possibleFollowupQuestions;

    public QuizChainInputDTO(String conversationId, String message, String studentId) {
        this.conversationId = conversationId;
        this.message = message;
        this.studentId = studentId;
    }

    public QuizChainInputDTO(String conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
    }

    public QuizChainInputDTO(String conversationId, String message, String studentId, List<String> possibleFollowupQuestions) {
        this.conversationId = conversationId;
        this.studentId = studentId;
        this.message = message;
        this.possibleFollowupQuestions = possibleFollowupQuestions;
    }

    public QuizChainInputDTO() {}

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPossibleFollowupQuestions() {
        return possibleFollowupQuestions;
    }

    public void setPossibleFollowupQuestions(List<String> possibleFollowupQuestions) {
        this.possibleFollowupQuestions = possibleFollowupQuestions;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "QuizChainInputDTO{" +
                "conversationId='" + conversationId + '\'' +
                ", message='" + message + '\'' +
                ", studentId='" + studentId + '\'' +
                ", possibleFollowupQuestions=" + possibleFollowupQuestions +
                '}';
    }
}
