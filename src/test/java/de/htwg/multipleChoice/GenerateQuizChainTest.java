package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
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
    @Inject
    ConversationRepository conversationRepository;

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

    @Test
    @TestTransaction
    void testChainForTextInput(){

        String result = generateQuizChain.startTheChain("Definition\n" +
                "Der Fachausschuss für Medizinische Informatik (FAMI) der Deutschen Gesellschaft für Medizinische Informatik, Biometrie und Epidemiologie e.V. (GMDS) beschreibt Medizinische Informatik wie folgt:\n" +
                "\n" +
                "„Medizin ist ohne eine umfassende und sorgfältig geplante Erhebung und Verarbeitung von Informationen nicht möglich. So beeinflusst in Krankenhäusern eine adäquate Informationslogistik wesentlich die Qualität der Patientenversorgung. Eine adäquate Präsentation und systematische Aufbereitung von Bild- und Biosignalbefunden kann diagnostische und therapeutische Entscheidungen unterstützen. Ebenso ist eine systematische Repräsentation von ärztlichem Wissen über die Diagnostik und Therapie von Erkrankungen und der Aufbau von Wissensbanken zur Entscheidungsunterstützung des Arztes hilfreich.\n" +
                "\n" +
                "Die Medizinische Informatik ist die Wissenschaft der systematischen Erschließung, Verwaltung, Aufbewahrung, Verarbeitung und Bereitstellung von Daten, Informationen und Wissen in der Medizin und im Gesundheitswesen. Sie ist von dem Streben geleitet, damit zur Gestaltung der bestmöglichen Gesundheitsversorgung beizutragen.\n" +
                "\n" +
                "Zu diesem Zweck setzt sie Theorien und Methoden, Verfahren und Techniken der Informatik und anderer Wissenschaften ein und entwickelt eigene. Mittels dieser beschreiben, modellieren, simulieren und analysieren Medizinische Informatiker Informationen und Prozesse mit dem Ziel,\n" +
                "\n" +
                "Ärzte, Pflegekräfte und andere Akteure im Gesundheitswesen sowie Patienten und Angehörige zu unterstützen,\n" +
                "Versorgungs- und Forschungsprozesse zu gestalten und zu optimieren sowie\n" +
                "zu neuem Wissen in Medizin und Gesundheitswesen beizutragen.\n" +
                "Damit die hierzu nötigen Daten und Informationen und das benötigte Wissen fachgerecht erfasst, aufbewahrt, abgerufen, verarbeitet und verteilt werden können, entwickeln, betreiben und evaluieren Medizinische Informatiker Infrastrukturen, Informations- und Kommunikationssysteme einschließlich solcher für Medizintechnische Geräte.\n" +
                "\n" +
                "Die Medizinische Informatik versteht diese als sozio-technische Systeme, deren Arbeitsweisen sich in Übereinstimmung mit ethischen, rechtlichen und ökonomischen Prinzipien befinden.“\n" +
                "\n" +
                "– Fachausschuss für Medizinische Informatik (FAMI) der GMDS: Definition Medizinische Informatik[1]", UUID.randomUUID());
        // check if the result is not null
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    void testChainForURLInput(){
        String result = generateQuizChain.startTheChain("hier ist die URL https://de.wikipedia.org/wiki/Medizinische_Informatik", UUID.randomUUID());

        System.out.println(result);
        assertNotNull(result);
        UUID validID = UUID.fromString(result);
        assertNotNull(mcQuizRepository.findById(validID));
    }

    @Test
    @TestTransaction
    void testChainForJustChat(){
        Conversation conversation = new Conversation();
        conversationRepository.persist(conversation);
        String result = generateQuizChain.startTheChain("Servus was besagt eig die berühmte Gleichung von Albert Einstein?", conversation.getId());
        System.out.println(result);
        assertNotNull(result);
    }


}