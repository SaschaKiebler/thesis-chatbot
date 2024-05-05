package de.htwg.rag.retriever;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.store.embedding.filter.Filter;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.inject.Singleton;

import java.util.function.Function;
import java.util.function.Supplier;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;
import static java.util.Arrays.asList;

@Singleton
public class RetrievalAugmentorWithStudentFilter implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor augmentor;


    // uses the PgVectorEmbeddingStore and the OpenAiEmbeddingModel.
    // The Store is a extension of the normal PostgresDB.
    public RetrievalAugmentorWithStudentFilter(PgVectorEmbeddingStore store, EmbeddingModel model) {

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
                .promptTemplate(PromptTemplate.from("Lese und verstehe das Gespräch zwischen dem Benutzer und dem KI. " +
                        "Analysiere dann die neue Anfrage des Benutzers. Identifiziere alle relevanten Details, " +
                        "Begriffe und den Kontext sowohl aus dem Gespräch als auch aus der neuen Anfrage. " +
                        "Formuliere diese Anfrage in ein klares, prägnantes und in sich geschlossenes Format um, " +
                        "das für die Informationssuche geeignet ist.\n" +
                        "\n" +
                        "Gespräch:\n" +
                        "{{chatMemory}}\n" +
                        "\n" +
                        "Benutzeranfrage: {{query}}\n" +
                        "\n" +
                        "Es ist sehr wichtig, dass du nur die umformulierte Anfrage und nichts anderes bereitstellst! Füge einer Anfrage nichts voran!"))
                .build();

        System.out.println(queryTransformer.toString());

        // ContentInjector to give metadata with the retrieved documents
        // comment the metadata injector out bc there is no reference to the documents injected by the user
        /*ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(asList("link"))
                .promptTemplate(PromptTemplate.from("{{userMessage}}\n\nAntworte unter Verwendung der folgenden Informationen und füge unter deiner Antwort einen Link zu den Dokumenten hinzu:\n{{contents}}"))
                .build();*/

        // In den Retriever kann man auch einen filter einbauen der nach metadaten filtert also z.B.
        // wenn bei Dokument als Metadaten steht Fach xy, könnte man in der Oberfläche ein Auswahlmenü
        // einbauen.
        // siehe https://github.com/langchain4j/langchain4j-examples/blob/main/rag-examples/src/main/java/_06_Metadata_Filtering.java

        // Filters by student ID, the id has to be in the metadata, the id gets transferred from AIService as @UserName
        Function<Query, Filter> filterByStudentID = query -> {
            try {
                return metadataKey("studentId").isEqualTo(query.metadata().userMessage().name());
            } catch (Exception e) {
                return null;
            }
        };

        // The Retriever to get the Documents from the store.
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever
                .builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .dynamicFilter(filterByStudentID)
                .maxResults(4)
                .minScore(0.75)
                .build();

        augmentor = DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(contentRetriever)
                .queryTransformer(queryTransformer)
            //    .contentInjector(contentInjector)   Auskommentiert
                .build();
    }

    @Override
    public RetrievalAugmentor get() {
        return augmentor;
    }

}
