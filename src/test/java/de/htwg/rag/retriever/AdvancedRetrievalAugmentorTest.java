package de.htwg.rag.retriever;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.rag.query.Metadata;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class AdvancedRetrievalAugmentorTest {

    @InjectMock
    PgVectorEmbeddingStore store;

    @InjectMock
    AllMiniLmL6V2QuantizedEmbeddingModel model;

    AdvancedRetrievalAugmentor advancedRetrievalAugmentor;

    @BeforeEach
    void setUp() {
        advancedRetrievalAugmentor = new AdvancedRetrievalAugmentor(store, model);
    }

    @Test
    void testGet() {
        assertNotNull(advancedRetrievalAugmentor.get());
    }

}