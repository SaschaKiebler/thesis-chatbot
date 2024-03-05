package de.htwg.rag.retriever;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.inject.Singleton;

import java.util.function.Supplier;

@Singleton
public class BasicRetrievalAugmentor implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor augmentor;

    public BasicRetrievalAugmentor(PgVectorEmbeddingStore store, AllMiniLmL6V2QuantizedEmbeddingModel model) {
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(3)
                .build();
        augmentor = DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(contentRetriever)
                .build();
    }

    @Override
    public RetrievalAugmentor get() {
        return augmentor;
    }
}
