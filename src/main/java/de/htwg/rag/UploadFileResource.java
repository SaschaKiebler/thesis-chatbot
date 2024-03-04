package de.htwg.rag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteFile(@PathParam("id") UUID id) {
        try {
            System.out.println("Deleting file with id: " + id + " from database at: " + new Date());
            uploadFileRepository.deleteById(id);
            return Response.ok().build();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }
}
