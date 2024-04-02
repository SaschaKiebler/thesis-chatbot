package de.htwg.multipleChoice.DTOs;

public class QuizChainInputDTO {

    private String conversationId;
    private String message;

    public QuizChainInputDTO(String conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
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
}
