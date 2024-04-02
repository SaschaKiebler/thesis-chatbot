package de.htwg.multipleChoice.DTOs.serviceDTOs;

import dev.langchain4j.model.output.structured.Description;

public class GenerateTheQuizDTO {

    @Description("the id of the quiz")
    String quizId;
    @Description("if the quiz with its questions was generated or not as a boolean value")
    Boolean success;
    @Description("the message to the user")
    String message;

    public GenerateTheQuizDTO(String quizId, Boolean success) {
        this.quizId = quizId;
        this.success = success;
    }

    public GenerateTheQuizDTO(Boolean success) {
        this.success = success;
    }

    public GenerateTheQuizDTO() {
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
