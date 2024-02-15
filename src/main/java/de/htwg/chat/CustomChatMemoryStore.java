package de.htwg.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CustomChatMemoryStore implements ChatMemoryStore {

    @Inject
    MessageRepository messageRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    ConversationRepository conversationRepository;


    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        List<ChatMessage> messages = new ArrayList<>();
        List<Message> listofUserInput = messageRepository.findByConversationId(UUID.fromString((String) memoryId));
        for (Message message : listofUserInput) {
            messages.add(new UserMessage(message.getMessage()));
            Answer answer = answerRepository.findByMessageId(message.getId());
            messages.add(new AiMessage(answer.getAnswer()));
        }

        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        List<ChatMessage> existungMessages = new ArrayList<>();
        List<Message> listofUserInput = messageRepository.findByConversationId((UUID) memoryId);
        for (Message message : listofUserInput) {
            existungMessages.add(new UserMessage(message.getMessage()));
            Answer answer = answerRepository.findByMessageId(message.getId());
            existungMessages.add(new AiMessage(answer.getAnswer()));
        }
        for (ChatMessage message : messages) {
            if (!existungMessages.contains(message)) {
                if (message instanceof UserMessage) {
                    Message msg = new Message.MessageBuilder()
                            .message(message.text())
                            .model("COMMERCIAL")
                            .conversation(conversationRepository.findById((UUID) memoryId))
                            .build();
                    messageRepository.persist(msg);
                } else {
                    Answer answer = new Answer.AnswerBuilder()
                            .answer(message.text())
                            .message(messageRepository.findByConversationId((UUID) memoryId).get(messageRepository.findByConversationId((UUID) memoryId).size() - 1))
                            .build();
                    answerRepository.persist(answer);
                }
            }
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        List<Message> listofUserInput = messageRepository.findByConversationId((UUID) memoryId);
        for (Message message : listofUserInput) {
            messageRepository.delete(message);
            Answer answer = answerRepository.findByMessageId(message.getId());
            answerRepository.delete(answer);
        }
    }
}
