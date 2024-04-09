package de.htwg.multipleChoice.tools;

import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserInputClassifierTest {


    @Test
    void classifyInputForNoData() {
        UserInputClassifier userInputClassifier = new UserInputClassifier();

        List<RequestType> result = userInputClassifier.classifyInput("Servus wie geht es dir");

        assertEquals(List.of(RequestType.NO_DATA), result);
    }

    @Test
    void classifyInputForQuestionNoData() {
        UserInputClassifier userInputClassifier = new UserInputClassifier();

        List<RequestType> result = userInputClassifier.classifyInput("Was ist Refactoring?");

        assertEquals(List.of(RequestType.NO_DATA), result);
    }

    @Test
    void classifyInputForText() {
        UserInputClassifier userInputClassifier = new UserInputClassifier();

        List<RequestType> result = userInputClassifier.classifyInput("Nomenklatur\n" +
                "▪ für bestimmten Bereiche verbindliche Sammlung von\n" +
                "Benennungen\n" +
                "▪ z.B. in der Informatik (Java)\n" +
                "▪ Klassennamen: Substantive, die mit einem\n" +
                "Großbuchstaben beginnen, z.B. „Professor“\n" +
                "▪ Methodennamen: Verben, die mit einem\n" +
                "Kleinbuchstaben beginnen, z.B. „add“ oder „remove“\n" +
                "▪ z.B. in der Medizin\n" +
                "▪ Terminologia Anatomica (TA)\n" +
                "▪ Körperteile, z.B. Bursa – Schleimbeutel und Os – Knochen\n" +
                "▪ Ortsangaben, z.B. dorsalis – Am Rücken gelegen, pectoralis\n" +
                "– an der Brust\n" +
                "▪ Richtungen und Größen, z.B. anterior – vorderer, minimus –\n" +
                "der Kleinste, superior – oberer\n" +
                "8\n" +
                "Nomenklatur\n" +
                "Kleine Übung:\n" +
                "Was bedeutet\n" +
                "„Bursitis subcutanea digitorum dorsalis“\n" +
                "• Bursa – Schleimbeutel\n" +
                "• -itis – Entzündung\n" +
                "• Subcutanea – „Unter der Haut“\n" +
                "• Digitus – Finger\n" +
                "• Dorsalis – Am Rücken gelegen,\n" +
                "hier Handrücken\n" +
                "Telemedizin und eHealth » Semantische Interoperabilität Prof. Dr. Christian Wache\n" +
                "▪ In der Informatik ist damit die\n" +
                "Beschreibung der Begriffe\n" +
                "(Konzepte) eines Fachgebiets\n" +
                "gemeint und deren Beziehungen\n" +
                "(Relationen) untereinander\n" +
                "▪ Wissensrepräsentation\n" +
                "▪ Relationen sind selbst Konzepte\n" +
                "▪ Logische Folgerungen!\n" +
                "9\n" +
                "Ontologie\n" +
                "Quelle: https://de.wikipedia.org/wiki/Ontologie_(Informatik)\n" +
                "Telemedizin und eHealth » Semantische Interoperabilität Prof. Dr. Christian Wache\n" +
                "▪ Klassifikationssystem\n" +
                "▪ Hierarchisch strukturiert\n" +
                "▪ Bezeichner (= Deskriptoren) sind nicht notwendig, da\n" +
                "natürlichsprachliche Wörter\n" +
                "▪ Keine Repräsentation lexikalischer Relationen\n" +
                "▪ Inhaltliche, hierarchische Strukturierung eines\n" +
                "Fachgebiets\n" +
                "10\n" +
                "Taxonomie\n" +
                "Quelle: https://de.wikipedia.org/wiki/Taxonomie\n" +
                "Telemedizin und eHealth » Semantische Interoperabilität Prof. Dr. Christian Wache\n" +
                "▪ Interoperabilität\n" +
                "▪ Semantische Interoperabilität\n" +
                "▪ Medizinische Ordnungssysteme\n" +
                "▪ ICD und OPS\n" +
                "▪ ICF\n" +
                "▪ LOINC\n" +
                "▪ SNOMED-CT\n" +
                "▪ Weitere Ordnungssysteme");
        assertEquals(List.of(RequestType.TEXT), result);
    }

    @Test
    void classifyInputForUrl() {
        UserInputClassifier userInputClassifier = new UserInputClassifier();

        List<RequestType> result = userInputClassifier.classifyInput("hier ist die Seite www.test.de");

        assertEquals(List.of(RequestType.URL), result);
    }
}