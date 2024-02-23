package de.htwg.llms.streaming;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.llms.OpenAIService;
import de.htwg.rag.DocumentRetriever;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.Cancellable;
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
import org.yaml.snakeyaml.emitter.Emitter;

import java.io.OutputStreamWriter;
import java.util.UUID;

import static dev.langchain4j.data.message.AiMessage.aiMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;


@Path("restApi")
@ApplicationScoped
public class OpenAIStreamingService {

    @GET
    public String hello() {
        return "Hello";
    }
//    StreamingChatLanguageModel model = OpenAiStreamingChatModel
//            .builder()
//            .apiKey(System.getenv("OPENAI_API_KEY"))
//            .build();
//    @Inject
//    CustomMemoryProvider provider;
//
//    @Inject
//    DocumentRetriever documentRetriever;
//
//
//
//    String answer = "";
//
//
//    @Inject
//    ConversationRepository conversationRepository;
//
//
//    @GET
//    @Path("chat")
//    @Produces(MediaType.TEXT_PLAIN)
//    @RestStreamElementType(MediaType.TEXT_PLAIN)
//    @Transactional
//    public void chatService(@Context SseEventSink eventSink,
//                                   @Context Sse sse,
//                                   @QueryParam("conversationId") String conversationId,
//                                   @QueryParam("message") String messageText) {
//
//        StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
//                .apiKey(System.getenv("OPENAI_API_KEY"))
//                .build();
//
//        OpenAIService openAIService = AiServices.builder(OpenAIService.class)
//                .chatMemoryProvider(provider.get())
//                .retriever(documentRetriever)
//                .streamingChatLanguageModel(model)
//                .build();
//
//        Conversation conversation;
//        if(conversationId != null && !conversationId.isEmpty()){
//            conversation = conversationRepository.findById(UUID.fromString(conversationId));
//        }
//        else{
//            conversation = new Conversation();
//            saveConversation(conversation);
//        }
//
//         TokenStream tokenStream = openAIService.chat(conversation.getId().toString(), messageText);
//
//
//Multi.createFrom().emitter(emitter -> tokenStream
//       .onNext(s -> {
//           answer += s;
//           System.out.println("onNext: " + s);
//           emitter.emit(answer);
//       })
//       .onComplete(response -> {
//            System.out.println("onComplete: " + response);
//           eventSink.close();
//       })
//       .onError(Throwable::printStackTrace)
//       .start()).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
//            .subscribe().with(
//                    item -> eventSink.send(sse.newEvent((String) item)),
//                    failure -> {
//                        System.out.println("Error in streaming response: " + failure.getMessage());
//                        eventSink.close();
//                    },
//                    () -> eventSink.close()
//            );
//
//    }
//
//    @Transactional
//    public void saveConversation(Conversation conversation){
//        conversationRepository.persist(conversation);
//    }

}
