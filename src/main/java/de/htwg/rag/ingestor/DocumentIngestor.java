package de.htwg.rag.ingestor;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

/**
 * This class is the Ingestor for Documents.
 * It ingests Documents into the Vectorstore.
 */
@ApplicationScoped
public class DocumentIngestor {
    @Inject
    PgVectorEmbeddingStore store;

    @Inject
    AllMiniLmL6V2QuantizedEmbeddingModel embeddingModel;

    /**
     * This method ingests a list of Documents into the Vectorstore.
     * @param documents The list of Documents to ingest.
     */
    public void ingest(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(300, 20))
                .build();
        if (documents.isEmpty()) {
            throw new IllegalArgumentException("No documents to ingest");
        }
        else {
            ingestor.ingest(documents);
        }

    }

    public void setStore(PgVectorEmbeddingStore store) {
        this.store = store;
    }

    public void setEmbeddingModel(AllMiniLmL6V2QuantizedEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
}
