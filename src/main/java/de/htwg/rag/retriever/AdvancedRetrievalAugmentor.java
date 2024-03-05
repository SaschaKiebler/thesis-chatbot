package de.htwg.rag.retriever;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import io.github.cdimascio.dotenv.Dotenv;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.inject.Singleton;

import java.util.function.Supplier;

import static java.util.Arrays.asList;

@Singleton
public class AdvancedRetrievalAugmentor implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor augmentor;

    public AdvancedRetrievalAugmentor(PgVectorEmbeddingStore store, AllMiniLmL6V2QuantizedEmbeddingModel model) {

        // chatmodel just for the query transformer, can be any model,
        // all it does is compress the input query's to one so that the retrieval is more accurate
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(Dotenv.load().get("OPENAI_API_KEY"))
                .modelName("gpt-3.5-turbo")
                .logRequests(true)
                .logResponses(true)
                .build();
        QueryTransformer queryTransformer = new CompressingQueryTransformer(chatModel);

        // ContentInjector to give metadata with the retrieved documents
        ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(asList("file_name", "index"))
                .build();

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(3)
                .build();


        augmentor = DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(contentRetriever)
                .queryTransformer(queryTransformer)
                .contentInjector(contentInjector)
                .build();
    }

    @Override
    public RetrievalAugmentor get() {
        return augmentor;
    }
}
