package de.htwg.multipleChoice.repositories;

import de.htwg.multipleChoice.entities.MCQuiz;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MCQuizRepositoryTest {


    @Inject
    MCQuizRepository mcQuizRepository;

    @Test
    void findAllQuizFromUser() {
        UUID userId = UUID.fromString("4729aad1-45eb-493d-b3eb-d25e9598f3ce");
        List<MCQuiz> mcQuizzes = mcQuizRepository.findAllFromStudent(userId);
        System.out.println(mcQuizzes.size());
        assertFalse(mcQuizzes.isEmpty());
    }
}