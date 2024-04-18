package de.htwg.chat.repositories;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ConversationRepositoryTest {

    @Inject
    ConversationRepository conversationRepository;

}