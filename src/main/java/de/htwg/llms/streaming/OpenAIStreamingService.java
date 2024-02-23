package de.htwg.llms.streaming;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.llms.OpenAIService;
import de.htwg.rag.DocumentRetriever;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.util.List;
import java.util.UUID;

import static dev.langchain4j.data.message.AiMessage.aiMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;


@Path("restApi")
@ApplicationScoped
public class OpenAIStreamingService {

    StreamingChatLanguageModel model = OpenAiStreamingChatModel
            .builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .build();
    @Inject
    CustomMemoryProvider provider;

    @Inject
    DocumentRetriever documentRetriever;



    String answer = "";


    @Inject
    ConversationRepository conversationRepository;

    @GET
    @Path("chat")
    @Produces(MediaType.TEXT_PLAIN)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public void chatService(@Context SseEventSink eventSink,
                            @Context Sse sse,
                            @QueryParam("conversationId") String conversationId,
                            @QueryParam("message") String messageText) {

        StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();

        OpenAIService openAIService = AiServices.builder(OpenAIService.class)
                .chatMemoryProvider(provider.get())
                .retriever(documentRetriever)
                .streamingChatLanguageModel(model)
                .build();

        Conversation conversation;
        if(conversationId != null && !conversationId.isEmpty()){
            conversation = conversationRepository.findById(UUID.fromString(conversationId)).await().indefinitely();
        }
        else{
            conversation = new Conversation();
            saveConversation(conversation);
        }

         TokenStream tokenStream = openAIService.chat(conversation.getId().toString(), messageText);


        Multi.createFrom().emitter(emitter -> tokenStream
                .onNext(s -> {
                    answer += s;
                    emitter.emit(answer);
                })
                .onComplete(response -> {
                    emitter.emit(conversation.getId().toString());  // only for test. Should be removed bc otherwise the chat will show id
                    System.out.println("onComplete: " + response);
                    emitter.complete();
                })
                .onError(Throwable::printStackTrace)
                .start()).subscribe().with(
            item -> eventSink.send(sse.newEvent((String) item)),
    failure -> {
        System.out.println("Error in streaming response: " + failure.getMessage());
        eventSink.close();
    },
    eventSink::close
            );

    }

    @Transactional
    public void saveConversation(Conversation conversation){
        conversationRepository.persist(conversation);
    }

}
