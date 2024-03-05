package de.htwg.rag.ingestor;

import de.htwg.rag.dataTools.Summarizer;
import de.htwg.rag.ingestor.DocumentIngestor;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;


@QuarkusTest
class DocumentIngestorTest {

    @InjectMock
    PgVectorEmbeddingStore store;

    @InjectMock
    Summarizer summarizer;


    DocumentIngestor documentIngestor;
    Document document1;
    Document document2;
    Document document3;
    List<TextSegment> textSegments;

    @BeforeEach
    void setUp() {
        documentIngestor = new DocumentIngestor();
        documentIngestor.setStore(store);
        documentIngestor.setEmbeddingModel(new AllMiniLmL6V2QuantizedEmbeddingModel());
        document1 = new Document("This is a test document");
        document2 = new Document("This is another test document");
        document3 = new Document("This is a third test document");

        textSegments = Arrays.asList(
                new TextSegment("This is a test document",Metadata.metadata("index",0)),
                new TextSegment("This is another test document",Metadata.metadata("index",0)),
                new TextSegment("This is a third test document", Metadata.metadata("index",0)));
    }

    @Test
    void testIngestForEmptyList() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            documentIngestor.ingest(Arrays.asList());
        });
    }

    @Test
    void testIngestForValidList() {
        List<Document> documents = Arrays.asList(document1, document2, document3);

        when(store.addAll(anyList(), eq(textSegments))).thenReturn(List.of("test","test","test"));
        when(summarizer.summarize(anyString())).thenReturn("This is a test document");

        assertDoesNotThrow(() -> {
            documentIngestor.ingest(documents);
        });

        verify(store, times(1)).addAll(anyList(), eq(textSegments));

    }

    @Test
    void testIngestForListOfOneDocument() {
        List<Document> documents = Arrays.asList(document1);
        List<TextSegment> textSegments = Arrays.asList(new TextSegment("This is a test document", Metadata.metadata("index",0)));
        when(summarizer.summarize(anyString())).thenReturn("test");

        when(store.addAll(anyList(), eq(textSegments))).thenReturn(List.of("test"));

        assertDoesNotThrow(() -> {
            documentIngestor.ingest(documents);
        });

        verify(store, times(1)).addAll(anyList(), eq(textSegments));

    }

}