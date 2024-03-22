package de.htwg.chat.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Date;

/**
 * This class is the Resource for the "Dokumente View".
 * It serves the dokumente Template.
 */
@Path("/dokumente")
@ApplicationScoped
public class IngestorViewResource {


    @Inject
    Template dokumente;

    /**
     * This method is called when a GET request is sent to /dokumente.
     * It returns the dokumente Template.
     * @return The dokumente Template.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getDokumente() {
        System.out.println("/dokumente abgerufen am: " + new Date());
        return dokumente.data("title", "dokumente");
    }
}
