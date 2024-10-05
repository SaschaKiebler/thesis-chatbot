package de.htwg.voiceMode.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/voiceMode")
@ApplicationScoped
public class VoiceModeUIResource {

    @Inject
    Template voiceModeBase;


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getVoiceModeBase() {
        return voiceModeBase.data("title", "Voice Mode");
    }
}
