package de.htwg.rag.retriever;

import dev.langchain4j.model.cohere.CohereScoringModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
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

    // uses the PgVectorEmbeddingStore and the AllMiniLmL6V2QuantizedEmbeddingModel.
    // The Store is a extension of the normal PostgresDB and the model is running locally.
    public AdvancedRetrievalAugmentor(PgVectorEmbeddingStore store, AllMiniLmL6V2QuantizedEmbeddingModel model) {

        // chatmodel just for the query transformer, can be any model,
        // all it does is compress the input query's to one so that the retrieval is more accurate
        // and logic from the chat-history gets taken into account
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("TOGETHERAI_API_KEY"))
                .baseUrl("https://api.together.xyz")
                .modelName("openchat/openchat-3.5-1210")
                .logRequests(true)
                .logResponses(true)
                .build();
        QueryTransformer queryTransformer = CompressingQueryTransformer.builder()
                .chatLanguageModel(chatModel)
                .promptTemplate(PromptTemplate.from("Lese und verstehe das Gespräch zwischen dem Benutzer und dem KI. Analysiere dann die neue Anfrage des Benutzers. Identifiziere alle relevanten Details, Begriffe und den Kontext sowohl aus dem Gespräch als auch aus der neuen Anfrage. Formuliere diese Anfrage in ein klares, prägnantes und in sich geschlossenes Format um, das für die Informationssuche geeignet ist.\n" +
                        "\n" +
                        "Gespräch:\n" +
                        "{{chatMemory}}\n" +
                        "\n" +
                        "Benutzeranfrage: {{query}}\n" +
                        "\n" +
                        "Es ist sehr wichtig, dass du nur die umformulierte Anfrage und nichts anderes bereitstellst! Füge einer Anfrage nichts voran!"))
                .build();

        // ContentInjector to give metadata with the retrieved documents
        ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(asList("link"))
                .promptTemplate(PromptTemplate.from("{{userMessage}}\n\nAnswer using the following information and add any helpful links to the end of your answer:\n{{contents}}"))
                .build();

        // ScoringModel to rank the retrieved documents (not in use bc of a bug in langchain4j)
        ScoringModel scoringModel = CohereScoringModel.withApiKey(System.getenv("COHERE_API_KEY"));
        ContentAggregator contentAggregator = ReRankingContentAggregator.builder()
                .scoringModel(scoringModel)
                .minScore(0.8)
                .build();


        // The normal Retriever to get the Documents from the store.
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(7)
                .minScore(0.75)
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
