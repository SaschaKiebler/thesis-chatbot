package de.htwg.rag.ingestor;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
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

    EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
            .apiKey(System.getenv("OPENAI_APIKEY"))
            .dimensions(1536)
            .modelName("text-embedding-3-small").build();


    /**
     * This method ingests a list of Documents into the Vectorstore.
     * @param documents The list of Documents to ingest.
     */
    public void ingest(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(new DocumentBySentenceSplitter(500,50))
                .build();
        if (documents.isEmpty()) {
            System.out.println("No documents to ingest");
            throw new IllegalArgumentException("No documents to ingest");
        }
        else {
            ingestor.ingest(documents);
        }

    }

    public void setStore(PgVectorEmbeddingStore store) {
        this.store = store;
    }

    public void setEmbeddingModel(EmbeddingModel model) {
        this.embeddingModel = model;
    }

}
