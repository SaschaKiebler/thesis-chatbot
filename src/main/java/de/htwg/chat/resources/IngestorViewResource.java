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

@Path("/dokumente")
@ApplicationScoped
public class IngestorViewResource {


    @Inject
    Template dokumente;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getDokumente() {
        System.out.println("/dokumente abgerufen am: " + new Date());
        return dokumente.data("title", "dokumente");
    }
}
