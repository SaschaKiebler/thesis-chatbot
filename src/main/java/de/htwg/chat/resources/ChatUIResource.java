package de.htwg.chat.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This class is the Resource for the Chat UI.
 * It serves the Chat UI.
 */
@Path("/chat")
@ApplicationScoped
public class ChatUIResource {


    @Inject
    Template chat;

    /**
     * This method is called when a GET request is sent to /chat.
     * It returns the Chat UI Template.
     * @return The Chat UI Template.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getChat() {
        return chat.instance();
    }
}
