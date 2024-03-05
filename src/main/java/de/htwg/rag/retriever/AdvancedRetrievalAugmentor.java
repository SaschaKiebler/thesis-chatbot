package de.htwg.rag.retriever;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
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
