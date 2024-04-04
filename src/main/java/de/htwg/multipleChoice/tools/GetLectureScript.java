package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
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

    /**
     * Method to get the script by its name. If the script is not found, an error message is returned.
     * @param scriptName
     * @return the text of the script if found, otherwise an error message.
     */
    @Tool("Get the script by name")
    public String RetrieveScriptByName(@P("The scriptName that the user provided") String scriptName) {
        if (scriptName == null) {
            return "Error: No script name provided. Please provide a valid script name.";
        }
        Script script = scriptRepository.findByName(scriptName);
        if (script == null) {
            return "No script found with the name: " + scriptName + ". Please check the name and try again.";
        }
        return script.getText();
    }

    /**
     * Method to get all scripts with similar name.
     * @param scriptName
     * @return the list of scripts if found, otherwise an empty list.
     */
    @Tool("Get all scripts with similar name")
    public List<Script> RetrieveAllScripts(@P("The scriptName that the user provided") String scriptName) {
        return scriptRepository.listAllSimilarToName(scriptName);
    }

}
