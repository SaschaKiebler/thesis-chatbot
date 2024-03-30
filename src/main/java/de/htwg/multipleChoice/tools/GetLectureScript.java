package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

/**
 * This class provides a method as a langchain4j Tool to get the lecture script.
 */
@ApplicationScoped
public class GetLectureScript {

    @Inject
    ScriptRepository scriptRepository;

    /**
     * Retrieves the lecture script by its ID.
     * @param scriptId the UUID of the script provided by the user.
     * @return the text of the script if found, otherwise an error message.
     */
    @Tool("Get the script by ID")
    public String RetrieveScriptById(@P("The scriptId that the user provided") String scriptId) {
        if (scriptId == null) {
            return "Error: No script ID provided. Please provide a valid script ID.";
        }

        Script script = scriptRepository.findById(UUID.fromString(scriptId));

        if (script == null) {
            return "No script found with the ID: " + scriptId + ". Please check the ID and try again.";
        }

        return script.getText();
    }

}
