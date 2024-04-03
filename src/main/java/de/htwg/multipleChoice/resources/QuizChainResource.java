package de.htwg.multipleChoice.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.QuizChainInputDTO;
import de.htwg.multipleChoice.GenerateQuizChain;
import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import io.quarkus.vertx.http.runtime.devmode.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

@Path("/api/quizChain")
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response startQuizChain(@RequestBody QuizChainInputDTO inputDTO) {
        Conversation conversation = null;
        if (!inputDTO.getConversationId().isEmpty()){
            UUID conversationUUID = UUID.fromString(inputDTO.getConversationId());
            conversation = conversationRepository.findById(conversationUUID);
        }
        if (conversation == null) {
            conversation = new Conversation();
            conversationRepository.persist(conversation);
        }
        String result = generateQuizChain.startTheChain(inputDTO.getMessage(), conversation.getId());
        UUID quizId;

        try {
            quizId = UUID.fromString(result);
        } catch (Exception e) {
            quizId = null;
        }

        if (quizId != null) {
            MCQuiz quiz = mcQuizRepository.findById(quizId);
            if (quiz == null) {
                System.out.println("No quiz found with id: " + quizId.toString());
                return Response.ok().entity(Json.object().put("answer", result).put("conversationId", conversation.getId().toString())).build();
            }
            System.out.println("Quiz found with id: " + quizId.toString());
            return Response.ok().entity(Json.object().put("quizId",quizId.toString()).put("conversationId",conversation.getId().toString()).build()).build();
        }

        System.out.println("No quiz found with id: " + result + " with message: " + inputDTO.getMessage());
        return Response.ok().entity(Json.object().put("answer", result).put("conversationId", conversation.getId().toString())).build();
    }
}
