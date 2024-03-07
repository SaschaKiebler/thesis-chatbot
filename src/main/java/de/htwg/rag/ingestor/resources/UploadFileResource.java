package de.htwg.rag.ingestor.resources;

import de.htwg.rag.ingestor.UploadFileRepository;
import de.htwg.rag.ingestor.UploadedFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.io.File;
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
    public Response getAllFiles() {
        return Response.ok(uploadFileRepository.listAll()).build();
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces("application/pdf")
    public Response downloadFile(@PathParam("id") UUID id) {
        UploadedFile file = uploadFileRepository.findById(id);
        if (file == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        File f = new File(file.getPath());
        System.out.println("Downloading file with id: " + id + " from database at: " + new Date());
        if (!f.exists()) {
            System.out.println("File not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(f).header( "inline" ,"attachment; filename=" + file.getName()).build();
    }
}
