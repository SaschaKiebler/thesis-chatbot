package de.htwg.chat.memory;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Message;
import de.htwg.chat.entities.SystemMessage;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.repositories.SystemMessageRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@ApplicationScoped
public class CustomChatMemoryStore implements ChatMemoryStore {


    MessageRepository messageRepository = new MessageRepository();

    AnswerRepository answerRepository = new AnswerRepository();

    ConversationRepository conversationRepository = new ConversationRepository();

    SystemMessageRepository systemMessageRepository = new SystemMessageRepository();


    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        UUID memoryIdUUID = UUID.fromString((String) memoryId);
        List<ChatMessage> messages = new ArrayList<>();
        if (memoryId == "default"){
            return messages;
        }
        getListOfMessages(memoryIdUUID, messages);

        return messages;
    }

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        if (memoryId == "default"){
            return;
        }
        UUID memoryIdUUID = UUID.fromString((String) memoryId);
        List<ChatMessage> existingMessages = new ArrayList<>();
        getListOfMessages(memoryIdUUID, existingMessages);
        for (ChatMessage message : messages) {
            if (!existingMessages.contains(message)) {
                if (message instanceof UserMessage) {
                    Message msg = new Message.MessageBuilder()
                            .message(message.text())
                            .model("COMMERCIAL")
                            .conversation(conversationRepository.findById(memoryIdUUID))
                            .build();
                    messageRepository.persist(msg);
                } else if (message instanceof AiMessage){
                    Answer answer = new Answer.AnswerBuilder()
                            .answer(message.text())
                            .model("COMMERCIAL")
                            .message(messageRepository.findByConversationId(memoryIdUUID).get(messageRepository.findByConversationId(memoryIdUUID).size() - 1))
                            .build();
                    answerRepository.persist(answer);
                }
                else{
                    de.htwg.chat.entities.SystemMessage systemMessage = new de.htwg.chat.entities.SystemMessage();
                    systemMessage.setMessage(message.text());
                    systemMessage.setConversation(conversationRepository.findById(memoryIdUUID));
                    systemMessageRepository.persist(systemMessage);
                }
            }
        }
    }

    private void getListOfMessages(UUID memoryId, List<ChatMessage> existingMessages) {
        List<Message> listofUserInput = messageRepository.findByConversationId(memoryId);
        de.htwg.chat.entities.SystemMessage systemMessage = systemMessageRepository.find("conversation.id", memoryId).firstResult();

        if(listofUserInput.isEmpty()){
            return;
        }
        else {
            if(systemMessage != null){
                existingMessages.add(new dev.langchain4j.data.message.SystemMessage(systemMessage.getMessage()));
            }
            for (Message message : listofUserInput) {
                existingMessages.add(new UserMessage(message.getMessage()));
                Answer answer = answerRepository.findByMessageId(message.getId());
                if(answer != null) {
                    existingMessages.add(new AiMessage(answer.getAnswer()));
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteMessages(Object memoryId) {
        List<Message> listofUserInput = messageRepository.findByConversationId(UUID.fromString((String) memoryId));
        for (Message message : listofUserInput) {
            messageRepository.delete(message);
            Answer answer = answerRepository.findByMessageId(message.getId());
            answerRepository.delete(answer);
        }
    }
}
