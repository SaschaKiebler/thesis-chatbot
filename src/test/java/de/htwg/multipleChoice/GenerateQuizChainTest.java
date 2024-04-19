package de.htwg.multipleChoice;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.entities.Student;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.StudentRepository;
import de.htwg.multipleChoice.services.GenerateTheQuizAIService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class GenerateQuizChainTest {

    @Inject
    GenerateTheQuizAIService generateTheQuizAIService;
    @Inject
    GenerateQuizChain generateQuizChain;
    @Inject
    MCQuizRepository mcQuizRepository;
    @Inject
    ConversationRepository conversationRepository;
    @Inject
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {

    }




    @Test
    @TestTransaction
    void testChainForTextInput(){
        Student student = new Student("Tester");
        studentRepository.persist(student);
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
                "– Fachausschuss für Medizinische Informatik (FAMI) der GMDS: Definition Medizinische Informatik[1]", UUID.randomUUID(), String.valueOf(student.getId()));
        // check if the result is not null
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    @TestTransaction
    void testChainForURLInput(){
        Student student = new Student("Tester");
        studentRepository.persist(student);

        String result = generateQuizChain.startTheChain("hier ist die URL https://de.wikipedia.org/wiki/Medizinische_Informatik", UUID.randomUUID(), String.valueOf(student.getId()));

        System.out.println(result);
        assertNotNull(result);
        UUID validID = UUID.fromString(result);
        assertNotNull(mcQuizRepository.findById(validID));
    }

    @Test
    @TestTransaction
    void testChainForURLInput2(){
        Student student = new Student("Tester");
        studentRepository.persist(student);

        String result = generateQuizChain.startTheChain("mach mal ein quiz zu folgender seite https://ki-campus.org/blog/chatgpt-hochschullehre", UUID.randomUUID(), String.valueOf(student.getId()));

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

        Student student = new Student("Tester");
        studentRepository.persist(student);

        String result = generateQuizChain.startTheChain("Servus was besagt eig die berühmte Gleichung von Albert Einstein?", conversation.getId(), String.valueOf(student.getId()));
        System.out.println(result);
        assertNotNull(result);
    }


}