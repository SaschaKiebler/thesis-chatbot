package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.QuizChainInputDTO;
import de.htwg.multipleChoice.DTOs.serviceDTOs.GetTheScriptDTO;
import de.htwg.multipleChoice.services.GetTheScriptAIService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class QuizChainResourceTest {

    @InjectMock
    ConversationRepository conversationRepository;
    @InjectMock
    GetTheScriptAIService getTheScriptAIService;

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

        QuizChainInputDTO inputDTO = new QuizChainInputDTO();
        inputDTO.setConversationId(conversationId.toString());
        inputDTO.setMessage("test");

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        given()
                .contentType("application/json")
                .body(inputDTO)
                .when()
                .post("/api/quizChain")
                .then()
                .statusCode(200)
                .body("quizId", notNullValue());
    }

    /**
     *  TODO Conversationflow for Script not found produces random quiz, should ask for a valid script instead
     */
    @Test
    @TestTransaction
    void startTheChainTestForDocumentNotFound() {
        UUID scriptuuid = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        GetTheScriptDTO getTheScriptDTO = new GetTheScriptDTO();
        getTheScriptDTO.setName("test");
        getTheScriptDTO.setSuccess(true);
        getTheScriptDTO.setText("es wurde kein Script gefunden");
        getTheScriptDTO.setScriptId(scriptuuid.toString());
        when(getTheScriptAIService.getTheScript(any(String.class),any(UUID.class))).thenReturn(getTheScriptDTO);

        QuizChainInputDTO inputDTO = new QuizChainInputDTO();
        inputDTO.setConversationId(conversationId.toString());
        inputDTO.setMessage("test");

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);

        when(conversationRepository.findById(any(UUID.class))).thenReturn(conversation);

        given()
                .contentType("application/json")
                .body(inputDTO)
                .when()
                .post("/api/quizChain")
                .then()
                .statusCode(200);
        // eigentlich sollte die antwort nicht null sein, da der Nutzer aufgefordert werden soll, ein gültiges Skript zu liefern.
    }
}