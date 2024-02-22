package de.htwg.llms.streaming;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.memory.CustomMemoryProvider;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.llms.OpenAIService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.Response;
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

import static dev.langchain4j.data.message.UserMessage.userMessage;


@Path("restApi")
@ApplicationScoped
public class OpenAIStreamingService {

    StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

    @Inject
    CustomMemoryProvider provider;

    @Inject
    ConversationRepository conversationRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    @Transactional
    public void getMessages(@Context SseEventSink eventSink,
                            @Context Sse sse, @QueryParam("conversationId") String conversationId, @QueryParam("message") String message){

        //Handle Conversation for Chatmemory. Should be a seperate service and not in the resource
        Conversation conversation;
        if(conversationId != null && !conversationId.isEmpty()){
            conversation = conversationRepository.findById(UUID.fromString(conversationId));
        }
        else{
            conversation = new Conversation();
            conversationRepository.persist(conversation);
        }

        // Add UserInput to ChatMemory, retriever has to be added. Make retriever optional to have the comparison
        if (message != null && !message.isEmpty()) {
            provider.get().get(conversation.getId().toString()).add(userMessage(message));
        }

        // Get Messages from ChatMemory
        List<ChatMessage> messages = provider.get().get(conversation.getId().toString()).messages();


        // Streams the response from the AI as SSE.
        Multi.createFrom().emitter(emitter -> model.generate(messages, new StreamingResponseHandler<AiMessage>() {
            String message = "";
            @Override
            public void onNext(String token) {
                message += token;
                emitter.emit(message);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                emitter.emit(conversation.getId().toString());  // only for test. Should be removed bc otherwise the chat will show id
                System.out.println("onComplete: " + response);
                emitter.complete();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                emitter.fail(error);
            }
        })).subscribe().with(
                item -> eventSink.send(sse.newEvent((String) item)),
                failure -> {
                    System.out.println("Error in streaming response: " + failure.getMessage());
                    eventSink.close();
                },
                eventSink::close
            );
    }

}
