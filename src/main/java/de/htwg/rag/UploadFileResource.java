package de.htwg.rag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

@ApplicationScoped
@Path("/api/files")
public class UploadFileResource {

    @Inject
    UploadFileRepository uploadFileRepository;

    @Path("/all")
    @Produces("application/json")
    @GET
    public List<UploadedFile> getAllFiles() {
        return uploadFileRepository.listAll();
    }
}
