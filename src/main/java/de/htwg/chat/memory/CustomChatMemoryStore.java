package de.htwg.chat.memory;

import de.htwg.chat.entities.Answer;
import de.htwg.chat.entities.Message;
import de.htwg.chat.entities.SystemPrompt;
import de.htwg.chat.repositories.AnswerRepository;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.chat.repositories.MessageRepository;
import de.htwg.chat.repositories.SystemPromptRepository;
import de.htwg.llms.Modeltype;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
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


    @Inject
    MessageRepository messageRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    SystemPromptRepository systemPromptRepository;


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

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        if (memoryId.equals("default")) {
            return;
        }

        UUID memoryIdUUID = UUID.fromString((String) memoryId);
        List<ChatMessage> existingMessages = getListOfMessages(memoryIdUUID);

        for (ChatMessage message : messages) {
            if (!existingMessages.contains(message)) {
                persistMessage(memoryIdUUID, message);
            }
        }
    }

    private void persistMessage(UUID memoryIdUUID, ChatMessage message) {
        if (message instanceof UserMessage) {
            persistUserMessage(memoryIdUUID, message);
        } else if (message instanceof AiMessage) {
            persistAiMessage(memoryIdUUID, message);
        } else {
            persistSystemMessage(memoryIdUUID, message);
        }
    }

    private void persistUserMessage(UUID memoryIdUUID, ChatMessage message) {
        Message msg = new Message.MessageBuilder()
                .message(message.text())
                .model(Modeltype.COMMERCIAL.toString())
                .conversation(conversationRepository.findById(memoryIdUUID))
                .build();
        messageRepository.persist(msg);
    }


    private void persistAiMessage(UUID memoryIdUUID, ChatMessage message) {
        Message lastMessage = getLastMessage(memoryIdUUID);
        Answer answer = new Answer.AnswerBuilder()
                .answer(message.text())
                .model(Modeltype.COMMERCIAL.toString())
                .message(lastMessage)
                .build();
        answerRepository.persist(answer);
    }


    private void persistSystemMessage(UUID memoryIdUUID, ChatMessage message) {
        SystemPrompt systemPrompt = new SystemPrompt();
        systemPrompt.setMessage(message.text());
        systemPrompt.setConversation(conversationRepository.findById(memoryIdUUID));
        systemPromptRepository.persist(systemPrompt);
    }

    private Message getLastMessage(UUID memoryIdUUID) {
        List<Message> messages = messageRepository.findByConversationId(memoryIdUUID);
        return messages.get(messages.size() - 1);
    }

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
