package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.enums.RequestType;
import dev.langchain4j.classification.EmbeddingModelTextClassifier;
import dev.langchain4j.classification.TextClassifier;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;


/**
 * Cheaper classifier but currently only the llm classifier works well
 */
@ApplicationScoped
public class UserInputClassifier {

    Map<RequestType, List<String>> possibleInputs = Map.of(
            RequestType.NO_DATA, List.of(
                    "Kannst du mir das Rezept für Spaghetti Carbonara erklären?",
                    "Erkläre mir die Relativitätstheorie.",
                    "Was sind die Hauptunterschiede zwischen einem Krokodil und einem Alligator?",
                    "Ich brauche Tipps, wie man effektiver lernt.",
                    "Kannst du die Geschichte des Römischen Reiches zusammenfassen?",
                    "Was ist der Sinn des Lebens?",
                    "Wie funktioniert eine Blockchain?",
                    "Beschreibe die Photosynthese.",
                    "Was sind die besten Methoden zur Stressbewältigung?",
                    "Erkläre mir, wie ein Verbrennungsmotor funktioniert.",
                    "Hallo, wie geht es dir heute?",
                    "Was hältst du von künstlicher Intelligenz?",
                    "Ich fühle mich heute etwas traurig.",
                    "Hast du Tipps für gute Filme?",
                    "Was ist dein Lieblingsbuch?",
                    "Erzähl mir einen Witz.",
                    "Wie wird das Wetter morgen?",
                    "Ich habe heute Geburtstag!",
                    "Was denkst du über Weltraumreisen?",
                    "Kannst du mir bei meinen Hausaufgaben helfen?"
            ),
            RequestType.TEXT, List.of("1. Grundlagen der Programmierung In der Programmierung ist ein Algorithmus eine Reihe von Anweisungen, die ausgeführt werden, um eine bestimmte Aufgabe zu erledigen. Jede Programmiersprache hat ihre eigene Syntax und Struktur, aber die Grundprinzipien des Algorithmus-Designs bleiben gleich. Wichtige Konzepte sind Schleifen, bedingte Anweisungen, Funktionen, Klassen und Datenstrukturen. Effiziente Algorithmen reduzieren die Rechenzeit und den Speicherbedarf, was besonders in der Entwicklung großer Softwareanwendungen wichtig ist. Ein tiefes Verständnis von Algorithmen ermöglicht es Entwicklern, komplexe Probleme effektiv zu lösen und optimierte Lösungen zu entwickeln.",

"2. Datenbanken und SQL Eine Datenbank ist ein systematisches Arrangement von Daten. Datenbanken machen Datenverwaltung einfacher, indem sie eine strukturierte Umgebung für Daten bieten. SQL (Structured Query Language) ist eine Standard-Programmiersprache für das Verwalten und Abrufen von Informationen aus relationalen Datenbanken. Grundlegende Befehle wie SELECT, INSERT, UPDATE, DELETE ermöglichen es, Daten zu manipulieren und abzufragen. Relationale Datenbanken organisieren Daten in Tabellen, wobei Beziehungen zwischen den Tabellen definiert werden, um komplexe Abfragen und Analysen zu erleichtern. Verständnis von Datenmodellierung, Normalisierung und Transaktionsmanagement sind entscheidend für das effiziente Arbeiten mit Datenbanken.",

        "3. Betriebssysteme Ein Betriebssystem (OS) ist Software, die Computerhardware und Software-Ressourcen verwaltet und allgemeine Dienste für Computerprogramme bereitstellt. Schlüsselfunktionen eines Betriebssystems umfassen die Verwaltung von Prozessen, Speicherverwaltung, Dateisystemverwaltung und Netzwerkkommunikation. Ein tiefes Verständnis von Betriebssystemkonzepten wie Scheduling-Algorithmen, Deadlocks, Speicherverwaltungstechniken (z.B. Paging, Segmentation) und Konzepte der parallelen Verarbeitung sind wesentlich für das Verständnis, wie moderne Computer und Netzwerke funktionieren.",

        "4. Künstliche Intelligenz und Maschinelles Lernen Künstliche Intelligenz (KI) befasst sich mit der Schaffung von Maschinen oder Software, die menschenähnliche Intelligenz und Verhalten aufweisen. Zentrale Bereiche der KI umfassen maschinelles Lernen, neuronale Netze, Deep Learning, Natürliche Sprachverarbeitung und Robotik. Maschinelles Lernen beinhaltet Algorithmen, die Computern ermöglichen, aus Daten zu lernen und Vorhersagen oder Entscheidungen ohne explizite Programmierung zu treffen. Wichtige Konzepte des maschinellen Lernens beinhalten überwachtes, unüberwachtes und verstärktes Lernen, sowie die Auswahl und Optimierung von Modellen und Algorithmen, um spezifische Probleme zu lösen.",

        "5. Computernetzwerke Computernetzwerke ermöglichen die Verbindung von Computern, " +
                "um Daten und Ressourcen zu teilen. Wichtige Konzepte in der Netzwerktechnologie" +
                " umfassen das OSI-Modell, TCP/IP-Protokolle, Routing, Switching, Netzwerksicherheit und " +
                "Wireless-Technologien. Das Verstehen der Funktionsweise von Netzwerken, einschließlich des Internets, " +
                "ist entscheidend, um die Kommunikation zwischen Computern zu ermöglichen und zu optimieren. " +
                "Dies umfasst das Verständnis von IP-Adressierung, Subnetting, Verschlüsselungstechniken und " +
                "Firewall-Konzepten, um sichere und effiziente Netzwerklösungen zu entwickeln."
            ),
            RequestType.URL, List.of("https://www.wikipedia.org",
                                    "www.example.com",
                                    "http://www.meinewebsite.de",
                                    "https://news.google.com",
                                    "www.youtube.com/watch?v=dQw4w9WgXcQ",
                                    "https://www.github.com",
                                    "http://meinblog.blogspot.com",
                                    "www.meinefirma.com/produkte",
                                    "https://www.openai.com",
                                    "https://www.deeplearning.ai",
                                    "https://deeplearning.ai"
    )
    );

    OpenAiEmbeddingModel openAiEmbeddingModel = OpenAiEmbeddingModel.builder()
            .modelName("text-embedding-3-small")
            .apiKey(System.getenv("OPENAI_APIKEY"))
            .build();

    TextClassifier<RequestType> classifier = new EmbeddingModelTextClassifier<>(openAiEmbeddingModel, possibleInputs);


    public List<RequestType> classifyInput(String input) {
        return classifier.classify(input);
    }

}
