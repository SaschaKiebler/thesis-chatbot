package de.htwg.multipleChoice.resources;

import de.htwg.chat.entities.Conversation;
import de.htwg.chat.repositories.ConversationRepository;
import de.htwg.multipleChoice.DTOs.QuizChainInputDTO;
import de.htwg.multipleChoice.GenerateQuizChain;
import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.services.PossibleFollowUpQuestionsAIService;
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

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Resources for the quizchain
 */
@Path("/api/quizChain")
@ApplicationScoped
public class QuizChainResource {

    @Inject
    GenerateQuizChain generateQuizChain;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    MCQuizRepository mcQuizRepository;

    @Inject
    PossibleFollowUpQuestionsAIService possibleFollowUpQuestionsAIService;


    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        return UUID_PATTERN.matcher(str).matches();
    }

    /**
     * This function makes the quizchain visible on the endpoint /api/quizChain
     *
     * @param  inputDTO	a ChainInputDTO containing the conversationId and the message
     * @return         	either the generated quizId or a message from the ai
     */
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response startQuizChain(@RequestBody QuizChainInputDTO inputDTO) {
        Conversation conversation = null;
        System.out.println(inputDTO.toString());
        if (!inputDTO.getConversationId().isEmpty()){
            UUID conversationUUID = UUID.fromString(inputDTO.getConversationId());
            conversation = conversationRepository.findById(conversationUUID);
        }
        if (conversation == null) {
            conversation = new Conversation();
            conversationRepository.persist(conversation);
        }
        String result = generateQuizChain.startTheChain(inputDTO.getMessage(), conversation.getId(), inputDTO.getStudentId());
        UUID quizId = null;

        // try to get the id of the generated quiz, if not return the message text to the user
        if (isValidUUID(result)) {
            quizId = UUID.fromString(result);
        }


        if (quizId != null) {
            MCQuiz quiz = mcQuizRepository.findById(quizId);
            if(quiz!=null) {
                System.out.println("Quiz found with id: " + quizId.toString());
                return Response.ok().entity(Json.object().put("quizId", quizId.toString()).put("conversationId", conversation.getId().toString()).build()).build();
            }
        }

        System.out.println("No quiz found. Answer with message: " + result);
        QuizChainInputDTO input = new QuizChainInputDTO(conversation.getId().toString(), result);

        // generate possible follow-up questions
        List<String> possibleFollowUpQuestions = possibleFollowUpQuestionsAIService.possibleQuestionsChat(result);
        input.setPossibleFollowupQuestions(possibleFollowUpQuestions);
        return Response.ok().entity(input).build();
    }
}
