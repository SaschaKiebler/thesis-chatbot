package de.htwg.rag.retriever;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class DocumentRetrieverTest {

    @InjectMock
    PgVectorEmbeddingStore store;

    @InjectMock
    AllMiniLmL6V2QuantizedEmbeddingModel model;

    DocumentRetriever documentRetriever;

    @BeforeEach
    void setUp() {
        documentRetriever = new DocumentRetriever(store, model);
    }

    @Test
    void testFindRelevantForInputTestReturnNoMatches() {
        Response<Embedding> response = Response.from(Embedding.from(new float[]{0.1f, 0.2f, 0.3f}));
        Embedding embeddedText = (Embedding) response.content();

        when(model.embed("test")).thenReturn(response);
        when(store.findRelevant(embeddedText, 5)).thenReturn(List.of());

         List<TextSegment> relevant = documentRetriever.findRelevant("test");
        assertEquals(0, relevant.size());
    }

    @Test
    void testFindRelevantForInputTestReturnOneMatch() {
        Response<Embedding> response = Response.from(Embedding.from(new float[]{0.1f, 0.2f, 0.3f}));
        Embedding embeddedText = (Embedding) response.content();
        List<EmbeddingMatch<TextSegment>> matches = List.of(mock(EmbeddingMatch.class));

        when(model.embed("test")).thenReturn(response);
        when(store.findRelevant(any(Embedding.class), anyInt(),anyInt())).thenReturn( matches);

        List<TextSegment> relevant = documentRetriever.findRelevant("test");
        assertEquals(0, relevant.size());
    }

    @Test
    void testFindRelevantForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> documentRetriever.findRelevant(""));
    }
}