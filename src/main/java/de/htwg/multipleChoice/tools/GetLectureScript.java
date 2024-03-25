package de.htwg.multipleChoice.tools;

import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.ScriptRepository;
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
     * This method returns the lecture script.
     * @param scriptId The id of the lecture.
     * @return The lecture script.
     */
    @Tool("get the lecture script by id")
    public String getLectureScriptById(String scriptId) {
        UUID uuid = UUID.fromString(scriptId);
        Script script = scriptRepository.findById(uuid);
        if (script == null) {
            return "No script found with id " + scriptId;
        }
        return script.getText();
    }
}
