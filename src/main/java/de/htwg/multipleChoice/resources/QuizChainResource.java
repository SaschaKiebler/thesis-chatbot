package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.GenerateQuizChain;
import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

@Path("/api/quizchain")
@ApplicationScoped
public class QuizChainResource {

    @Inject
    GenerateQuizChain generateQuizChain;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    MCQuizRepository mcQuizRepository;

    @POST
    @Transactional
    public Response startQuizChain(@FormParam("message") String message, @FormParam("conversationId") String conversationId) {
        Conversation conversation = null;
        if (!conversationId.isEmpty()){
            UUID conversationUUID = UUID.fromString(conversationId);
            conversation = conversationRepository.findById(conversationUUID);
        }

        if (conversation == null) {
            conversation = new Conversation();
            conversationRepository.persist(conversation);
        }
        String result = generateQuizChain.startTheChain(message, conversation.getId());
        UUID quizId;

        try {
            quizId = UUID.fromString(result);
        } catch (Exception e) {
            quizId = null;
        }

        if (quizId != null) {
            MCQuiz quiz = mcQuizRepository.findById(quizId);
            if (quiz == null) {
                return Response.ok("answer",result).build();
            }
            return Response.ok().entity(Json.object().put("quiz", quiz.toString()).build()).build();
        }

        return Response.ok("answer",result).build();

    }
}
