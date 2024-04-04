package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GetTheScriptDTO;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.services.GenerateTheQuizAIService;
import de.htwg.multipleChoice.services.GetTheScriptAIService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GenerateQuizChainTest {

    @InjectMock
    GetTheScriptAIService getTheScriptAIService;
    @Inject
    GenerateTheQuizAIService generateTheQuizAIService;
    @Inject
    GenerateQuizChain generateQuizChain;
    @Inject
    MCQuizRepository mcQuizRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @TestTransaction
    void startTheChainTestForValidInput() {
        UUID scriptuuid = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        GetTheScriptDTO getTheScriptDTO = new GetTheScriptDTO();
        getTheScriptDTO.setName("test");
        getTheScriptDTO.setSuccess(true);
        getTheScriptDTO.setText("Die Krankenhausreform 2023 ist ein geplantes Reformpaket in Deutschland, das darauf abzielt, die Finanzierung und die Qualität der Versorgung in Krankenhäusern zu verbessern. Zu den geplanten Maßnahmen zählen unter anderem die Einführung einer Pauschalierung der Krankenhausleistungen, die Stärkung der ambulanten Versorgung in Krankenhäusern sowie die Schaffung von Anreizen für eine verbesserte Qualität und Patientensicherheit. Die Reform soll dazu beitragen, die Wirtschaftlichkeit und Effizienz im Krankenhaussektor zu erhöhen und den sich wandelnden Bedürfnissen der Patienten gerecht zu werden. Derzeit wird das Reformpaket von der Bundesregierung und den beteiligten Akteuren im Gesundheitswesen diskutiert und weiterentwickelt.\"");
        getTheScriptDTO.setScriptId(scriptuuid.toString());
        when(getTheScriptAIService.getTheScript(any(String.class),any(UUID.class))).thenReturn(getTheScriptDTO);


        String result = generateQuizChain.startTheChain("test", conversationId);

        // check if the result is not null
        assertNotNull(result);

        // check if the quiz was generated
        assertDoesNotThrow(() -> mcQuizRepository.findById(UUID.fromString(result)));

        // check if the quiz has at least one question
        assertFalse(mcQuizRepository.findById(UUID.fromString(result)).getQuestions().isEmpty());
    }

    @Test
    @TestTransaction
    void testStartTheChainTestForDocumentNotFound() {
        UUID conversationId = UUID.randomUUID();
        GetTheScriptDTO getTheScriptDTO = new GetTheScriptDTO();
        getTheScriptDTO.setName("test");
        getTheScriptDTO.setSuccess(true);
        getTheScriptDTO.setText("das Script wurde nicht gefunden");
        when(getTheScriptAIService.getTheScript(any(String.class),any(UUID.class))).thenReturn(getTheScriptDTO);

        String result = generateQuizChain.startTheChain("test", conversationId);

        // check if the result is not null
        assertNotNull(result);

    }
}