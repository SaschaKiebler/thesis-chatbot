package de.htwg.llms.streaming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import static dev.langchain4j.data.message.AiMessage.aiMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;

/**
 * This class was a test to implement the streaming service but doesn't work in any way.
 */
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
