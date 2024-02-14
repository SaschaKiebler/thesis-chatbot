package de.htwg.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DocumentRetriever implements Retriever<TextSegment> {
    private final EmbeddingStoreRetriever retriever;

    DocumentRetriever(PgVectorEmbeddingStore store, AllMiniLmL6V2QuantizedEmbeddingModel model) {
        // maxResults can be adapted, the bigger, the more context gets send to the AI
        retriever = EmbeddingStoreRetriever.from(store, model, 5);
    }

    @Override
    public List<TextSegment> findRelevant(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("No input provided");
        }
        return retriever.findRelevant(s);
    }
}
