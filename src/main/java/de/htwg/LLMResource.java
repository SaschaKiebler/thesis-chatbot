package de.htwg;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/llm")
@ApplicationScoped
public class LLMResource {

    @Inject
    OpenAIService openAIService;

    @Inject
    TogetherAIService togetherAIService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String sendRequestCommercial(@QueryParam("message") String message) {
        return openAIService.chat(message);
    }

    @POST
    @Path("/opensource")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendRequestOpenSource(@QueryParam("message") String message) {
        return togetherAIService.chat(message);
    }

}
