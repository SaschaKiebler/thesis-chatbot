package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.entities.MCQuiz;
import de.htwg.multipleChoice.entities.MultipleChoiceQuestion;
import de.htwg.multipleChoice.entities.PossibleAnswer;
import de.htwg.multipleChoice.repositories.MCQuizRepository;
import de.htwg.multipleChoice.repositories.MultipleChoiceQuestionRepository;
import de.htwg.multipleChoice.repositories.PossibleAnswerRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class provides a method as a langchain4j Tool to generate multiple choice questions for a quiz.
 */
@ApplicationScoped
public class QuizTools {

    @Inject
    MCQuizRepository mcQuizRepository;
    @Inject
    MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    @Inject
    PossibleAnswerRepository possibleAnswerRepository;

    /**
     * This method is a tool for an AiService to send a multiple choice question to the user.
     * @param question The question for the user.
     * @param answer1 The first possible answer.
     * @param answer2 The second possible answer.
     * @param answer3 The third possible answer.
     * @param correctAnswer The number of the correct answer.
     * @param quizId The id of the quiz the question belongs to.
     *
     */
    @Tool("Adds the question to the quiz. Before using this tool, create a new quiz. Make sure to send use the right format. Also be sure that the correct answer is one of the three possible answers. give the answer and questions in german.")
    @Transactional
    public void addQuestionToQuiz(@P("the question for the user") String question,
                                   @P("the first possible answer") String answer1,
                                   @P("the second possible answer") String answer2,
                                   @P("the third possible answer") String answer3,
                                   @P("the number of the correct answer") int correctAnswer,
                                   @P("The Quiz Id") String quizId) {

        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        // persist the answers and add them to the list
        PossibleAnswer possibleAnswer = new PossibleAnswer(answer1, correctAnswer == 1);
        possibleAnswerRepository.persist(possibleAnswer);
        possibleAnswers.add(possibleAnswer);
        PossibleAnswer possibleAnswer2 = new PossibleAnswer(answer2, correctAnswer == 2);
        possibleAnswerRepository.persist(possibleAnswer2);
        possibleAnswers.add(possibleAnswer2);
        PossibleAnswer possibleAnswer3 = new PossibleAnswer(answer3, correctAnswer == 3);
        possibleAnswerRepository.persist(possibleAnswer3);
        possibleAnswers.add(possibleAnswer3);

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(question, possibleAnswers);
        multipleChoiceQuestionRepository.persist(multipleChoiceQuestion);
        MCQuiz mcQuiz = mcQuizRepository.findById(UUID.fromString(quizId));
        mcQuizRepository.persist(mcQuiz);
        mcQuiz.addQuestion(multipleChoiceQuestion);
    }

    /**
     * Method to create a new quiz
     * @return the id of the new quiz
     */
    @Tool("Creates a new Quiz for the User and gives the quizId as UUID back")
    @Transactional
    public UUID createNewQuiz() {
        MCQuiz mcQuiz = new MCQuiz();
        mcQuizRepository.persist(mcQuiz);
        return mcQuiz.getId();
    }



}
