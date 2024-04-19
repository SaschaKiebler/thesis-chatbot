package de.htwg.multipleChoice.services;

import de.htwg.multipleChoice.DTOs.LectureDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LectureClassifierAIServiceTest {

    @Inject
    private LectureClassifierAIService lectureClassifierAIService;


    @Test
    void classifyTextForLectureTheoretischeInformatik() {
        String input = "Künstliche Intelligenz (KI), auch artifizielle Intelligenz (AI), englisch artificial intelligence, ist ein Teilgebiet der Informatik, das sich mit der Automatisierung intelligenten Verhaltens und dem maschinellen Lernen befasst. Der Begriff ist schwierig zu definieren, da es bereits an einer genauen Definition von „Intelligenz“ mangelt. Dennoch wird er in Forschung und Entwicklung verwendet.\n" +
                "\n" +
                "Ein Versuch der Definition von „Intelligenz“ ist, dass sie die Eigenschaft sei, die ein Wesen befähigt, angemessen und vorausschauend in seiner Umgebung zu agieren; dazu gehört die Fähigkeit, Umgebungsdaten wahrzunehmen, d. h. Sinneseindrücke zu haben und darauf zu reagieren, Informationen aufzunehmen, zu verarbeiten und als Wissen zu speichern, Sprache zu verstehen und zu erzeugen, Probleme zu lösen und Ziele zu erreichen.\n" +
                "\n" +
                "Praktische Erfolge der KI werden schnell in die Anwendungsbereiche integriert und zählen dann nicht mehr zur KI. Wegen dieses sogenannten „KI-Effekts“[1] scheint die KI-Forschung sich nur mit harten Nüssen abzumühen, die sie nicht knacken kann, was auch Teslers „Theorem“ zum Ausdruck bringt: „Intelligenz ist das, was Maschinen noch nicht gemacht haben“.";
        List<String> lectureNames = new ArrayList<>(List.of(
                "Theoretische Informatik",
                "Datenbanken",
                "Mathe 1",
                "Mathe 2",
                "Software Engineering"
        ));
        LectureDTO result = lectureClassifierAIService.classifyInput(input, lectureNames);
        assertEquals("Theoretische Informatik", result.getName());
    }

    @Test
    void classifyTextForLectureSoftwareEngineeringSecond() {
        String input = "Das DRY-Prinzip lautet: Don’t Repeat Yourself – Wiederhole dich nicht. Es gilt seit den Anfängen der Softwareentwicklung – sonst gäbe es keine Unterprogramme und keine Datennormalisierung. Dennoch ist es wahrscheinlich das am meisten missachtete Prinzip. Denn nichts ist einfacher, als Code durch Copy&Paste zu wiederholen. Gerade dann, wenn es mal schnell gehen soll, passiert das allzuoft.\n" +
                "\n" +
                "Clean Code Developer üben sich im roten Grad daher darin, dieses Prinzip stets zu beachten. Sie sind sich bewusst, wann sie Code oder andere Artefakte wiederholen. Sie erkennen solche Wiederholungen, die sie selbst oder andere erzeugt haben. Sie bereinigen Wiederholungen durch Refaktorisierungen – wenn keine anderen Prinzipien oder Beschränkungen dagegen sprechen.";
        List<String> lectureNames = new ArrayList<>(List.of(
                "Theoretische Informatik",
                "Datenbanken",
                "Mathe 1",
                "Mathe 2",
                "Software Engineering 1",
                "Software Engineering 2"
        ));
        LectureDTO result = lectureClassifierAIService.classifyInput(input, lectureNames);
        assertEquals("Software Engineering 1", result.getName());
    }

    @Test
    void classifyTextForLectureSoftwareEngineering() {
        String input = "Oder um es mit Albert Einsteins Worten zu sagen: „Alles sollte so einfach wie möglich gemacht werden, aber nicht einfacher.“. Für die Wandelbarkeit des Codes ist zwingende Voraussetzung, dass der Code verständlich ist. Eine einfache, klare und leicht verständliche Lösung sollte daher immer bevorzugt werden. Wenn man seinen eigenen Code nach kurzer Zeit schon nicht mehr versteht, sollten die Alarmglocken klingen. Noch wichtiger aber ist, dass auch andere Entwickler den Code schnell verstehen können. Dabei helfen regelmäßige Reviews und Pair Programming. Sie dienen der Kontrolle, ob tatsächlich die einfachste Lösung verwendet wurde. Gerade in technischen Details steckt die Versuchung, eine komplizierte Lösung anzustreben. Das Bekannte, naheliegende ist manchmal zu „langweilig“ – und schon hat sich eine komplizierte Lösung eingeschlichen. Wenn die einfache Lösung auch funktioniert, sollte ihr Vorrang gewährt werden. Das gleiche gilt für Datenstrukturen. Wenn ein IEnumerable reicht, sollte keine ICollection oder sogar IList verwendet werden.";
        List<String> lectureNames = new ArrayList<>(List.of(
                "Theoretische Informatik",
                "Datenbanken",
                "Mathe 1",
                "Mathe 2",
                "Software Engineering 1",
                "Software Engineering 2"
        ));
        LectureDTO result = lectureClassifierAIService.classifyInput(input, lectureNames);
        assertEquals("Software Engineering 1", result.getName());
    }
}