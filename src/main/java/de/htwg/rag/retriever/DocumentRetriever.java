package de.htwg.rag.retriever;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * This class is the Retriever for Documents.
 * It retrieves Documents from the Vectorstore.
 * @deprecated This class is deprecated because the newer versions of langchain4j have a different way of retrieving documents.

 */
@ApplicationScoped
@Deprecated
public class DocumentRetriever implements Retriever<TextSegment> {
    private final EmbeddingStoreRetriever retriever;

    public DocumentRetriever(PgVectorEmbeddingStore store, EmbeddingModel model) {
        // maxResults can be adapted, the bigger, the more context gets send to the AI
        // minScore can be adapted, the smaller, the more likely the context will be unrelated to the question
        retriever = EmbeddingStoreRetriever.from(store, model, 5,0.7);
    }

    @Override
    public List<TextSegment> findRelevant(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("No input provided");
        }
        return retriever.findRelevant(s);
    }
}
