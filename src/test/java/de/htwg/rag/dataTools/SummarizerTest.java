package de.htwg.rag.dataTools;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SummarizerTest {

    @Inject
    Summarizer summarizer;

    @Test
    void testSummarizeForEmptyText() {
        assertThrows(IllegalArgumentException.class, () -> summarizer.summarize(""));
    }

    @Test
    void testSummarizeForNullText() {

        assertThrows(IllegalArgumentException.class, () -> summarizer.summarize(null));
    }

    @Test
    void testSummarizeForValidText() {
        String text = "This is a test text";
        String result = summarizer.summarize(text);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testSummarizeTextsplitter() {
        String text = "Lorem ipsum dolor sit amet,\n" +
                "consectetur adipiscing elit.\n " +
                "Sed do eiusmod \n" +
                "tempor incididunt \n" +
                "ut labore et \n" +
                "dolore magna aliqua.\n" +
                " Ut enim ad minim veniam,\n" +
                " quis nostrud \n" +
                "exercitation \n" +
                "ullamco laboris nisi ut aliquip \n" +
                "ex ea commodo consequat. \n" +
                "Duis aute irure dolor in reprehenderit in \n" +
                "voluptate velit esse \n" +
                "cillum dolore eu fugiat \n" +
                "nulla pariatur. \n" +
                "Excepteur sint occaecat \n" +
                "cupidatat non proident,\n" +
                " sunt in culpa qui officia deserunt\n" +
                "consectetur adipiscing elit.\n " +
                "Sed do eiusmod \n" +
                "tempor incididunt \n" +
                "ut labore et \n" +
                "dolore magna aliqua.\n" +
                " Ut enim ad minim veniam,\n" +
                " quis nostrud \n" +
                "exercitation \n" +
                "ullamco laboris nisi ut aliquip \n" +
                "ex ea commodo consequat. \n" +
                "Duis aute irure dolor in reprehenderit in \n" +
                "voluptate velit esse \n" +
                "cillum dolore eu fugiat \n" +
                "nulla pariatur. \n" +
                "Excepteur sint occaecat \n" +
                "cupidatat non proident,\n" +
                " sunt in culpa qui officia deserunt\n" +
                " mollit anim id est laborum.\n";
        String result = summarizer.summarize(text);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}