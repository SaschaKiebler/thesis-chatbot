package de.htwg.multipleChoice;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This class is the Resource for the "Multiple Choice View".
 * It serves the multipleChoice Template.
 */
@Path("/multipleChoice")
@ApplicationScoped
public class MultipleChoiceUIResource {

    @Inject
    Template multipleChoice;

    /**
     * This method is called when a GET request is sent to /multipleChoice.
     * It returns the multipleChoice Template.
     * @return The multipleChoice Template.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getMultipleChoice() {
        return multipleChoice.instance();
    }
}
