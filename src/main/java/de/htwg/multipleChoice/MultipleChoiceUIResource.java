package de.htwg.multipleChoice;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/multipleChoice")
@ApplicationScoped
public class MultipleChoiceUIResource {

    @Inject
    Template multipleChoice;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getMultipleChoice() {
        return multipleChoice.instance();
    }
}
