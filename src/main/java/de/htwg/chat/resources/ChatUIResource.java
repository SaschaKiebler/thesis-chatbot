package de.htwg.chat.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
@ApplicationScoped
public class ChatUIResource {


    @Inject
    Template chat;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getChat() {
        return chat.instance();
    }
}
