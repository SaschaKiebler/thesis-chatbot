package de.htwg.chat.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class SystemPrompt {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String message;

    @ManyToOne(targetEntity = Conversation.class)
    private Conversation conversation;

    public SystemPrompt() {
    }

    public SystemPrompt(String message) {
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public SystemPrompt(SystemPromptBuilder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.conversation = builder.conversation;
    }

    public static SystemPromptBuilder builder() {
        return new SystemPromptBuilder();
    }

    public static class SystemPromptBuilder {
        private UUID id;
        private String message;
        private Conversation conversation;

        public SystemPromptBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public SystemPromptBuilder message(String message) {
            this.message = message;
            return this;
        }

        public SystemPromptBuilder conversation(Conversation conversation) {
            this.conversation = conversation;
            return this;
        }

        public SystemPrompt build() {
            return new SystemPrompt(this);
        }
    }


}
