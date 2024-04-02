package de.htwg.chat.memory;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.entities.Message;
import de.htwg.chat.entities.SystemPrompt;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.repositories.SystemPromptRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This class is a custom implementation of the ChatMemoryStore interface.
 * It is used to store the messages in a database.
 * It uses MessageRepository, AnswerRepository, ConversationRepository and SystemPromptRepository to persist the messages in the database.
 */
@ApplicationScoped
@DefaultBean
public class CustomChatMemoryStore implements ChatMemoryStore {


    @Inject
    MessageRepository messageRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    SystemPromptRepository systemPromptRepository;



    /**
     * This method is used to get the messages from the database.
     * It uses the conversation id.
     * It returns a list of ChatMessages.
     *
     * @param memoryId the conversation id
     * @return a list of ChatMessages
     */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {

        List<ChatMessage> messages = new ArrayList<>();

        // in some cases the memoryId was "default"
        if (memoryId == "default" || memoryId == "") {
            return messages;
        }
        UUID memoryIdUUID = UUID.fromString((String) memoryId);

        messages.addAll(getListOfMessages(memoryIdUUID));

        return messages;
    }

    /**
     * This method is used to update the messages in the database.
     * It uses the conversation id and the list of ChatMessages.
     * It persists the messages in the database.
     *
     * @param memoryId the conversation id
     * @param messages a list of ChatMessages
     */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        if (memoryId.equals("default")) {
            return;
        }

        UUID memoryIdUUID = UUID.fromString((String) memoryId);

        if(messages.size() > 0) {
            ChatMessage message = messages.get(messages.size() - 1);
            persistMessage(memoryIdUUID, message);
        }
    }

    /**
     * This method calls the persist method for the different types of messages.
     *
     * @param memoryIdUUID the conversation id
     * @param message a ChatMessage
     */
    private void persistMessage(UUID memoryIdUUID, ChatMessage message) {
        if (message instanceof UserMessage) {
            persistUserMessage(memoryIdUUID, message);
        } else if (message instanceof AiMessage) {
            persistAiMessage(memoryIdUUID, message);
        } else {
            persistSystemMessage(memoryIdUUID, message);
        }
    }

    /**
     * This method is used to persist a user message in the database.
     *
     * @param memoryIdUUID the conversation id
     * @param message a ChatMessage
     */
    private void persistUserMessage(UUID memoryIdUUID, ChatMessage message) {
        Message msg = Message.builder()
                .message(message.text())
                .conversation(conversationRepository.findById(memoryIdUUID))
                .build();
        messageRepository.persist(msg);
    }

    /**
     * This method is used to persist an ai message in the database.
     *
     * @param memoryIdUUID the conversation id
     * @param message a ChatMessage
     */
    private void persistAiMessage(UUID memoryIdUUID, ChatMessage message) {
        Message lastMessage = getLastMessage(memoryIdUUID);
        Answer answer = Answer.builder()
                .answer(message.text())
                .message(lastMessage)
                .build();
        answerRepository.persist(answer);
    }

    /**
     * This method is used to persist a system message in the database.
     *
     * @param memoryIdUUID the conversation id
     * @param message a ChatMessage
     */
    private void persistSystemMessage(UUID memoryIdUUID, ChatMessage message) {
        Conversation conversation = conversationRepository.findById(memoryIdUUID);
        SystemPrompt systemPrompt = SystemPrompt.builder().message(message.text()).conversation(conversation).build();
        systemPromptRepository.persist(systemPrompt);
    }

    /**
     * This method is used to get the latest message from the Conversation.
     *
     * @param memoryIdUUID the conversation id
     * @return the last message
     */
    private Message getLastMessage(UUID memoryIdUUID) {
        List<Message> messages = messageRepository.findByConversationId(memoryIdUUID);
        try {
            return messages.get(messages.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * This method is used to get all messages from a conversation.
     *
     * @param memoryId the conversation id
     * @return a list of ChatMessages
     */
    private List<ChatMessage> getListOfMessages(UUID memoryId) {
        List<ChatMessage> listOfMessages = new ArrayList<>();
        // get all messages from the database that where inputs from the user with the conversation id
        List<Message> listofUserInput = messageRepository.findByConversationId(memoryId);
        // get the system prompt for the conversation id (not the prettiest solution but somehow the prompt has to get into the list of messages)
        SystemPrompt systemPrompt = systemPromptRepository.findByConversationId(memoryId);

        // check if its a new conversation, if not, add the system prompt to the list of messages with all answers from the ai
        if (!listofUserInput.isEmpty()) {
            if (systemPrompt != null) {
                listOfMessages.add(new SystemMessage(systemPrompt.getMessage()));
            }
            for (Message message : listofUserInput) {
                listOfMessages.add(new UserMessage(message.getMessage()));
                Answer answer = answerRepository.findByMessageId(message.getId());
                if (answer != null) {
                    listOfMessages.add(new AiMessage(answer.getAnswer()));
                }
            }
        }
        return listOfMessages;
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // stays empty because there is no use-case doing deletes on the db from here
    }

}
