package de.htwg.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.PartType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Path("/api/ingest")
@ApplicationScoped
public class IngestDocumentResource {

    @Inject
    DocumentIngestor documentIngestor;

    @Inject
    UploadFileRepository uploadFileRepository;

    private static final String UPLOAD_DIRECTORY = "resources/pdfs";

    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response uploadPdf(@MultipartForm PdfFile pdfFile, @FormParam("name") String name) {
        if (isFileEmpty(pdfFile)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("file is required").build();
        }
        String path = "";
        try {
            // try to create a new file and safe the pdf to the UPLOAD_DIRECTORY
            String filePath = UPLOAD_DIRECTORY + "/" + name;
            path = safeFile(filePath, pdfFile.file);

            // create a new UploadedFile and persist it
            UploadedFile uploadedFile = new UploadedFile(name, path);
            uploadFileRepository.persist(uploadedFile);

            // load the document with the documentParser and ingest it as a list,
            // mabye add the possibility to send multiple files at once
            Document document = FileSystemDocumentLoader.loadDocument(path, new ApachePdfBoxDocumentParser());
            document.metadata().add("fileKey", uploadedFile.getId().toString());
            documentIngestor.ingest(List.of(document));
            return Response.ok().build();

        } catch (Exception e) {
            try {
                Files.deleteIfExists(java.nio.file.Path.of(path));
            } catch (IOException ex) {
                System.err.println("Error deleting file after exception");
            }
            throw new RuntimeException("Error saving file", e);
        }
    }

    // adds date to filename to make it unique and safes it in the file system
    private String safeFile(String filePath, byte[] file) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String formattedDate = formatter.format(new Date());
        String path = filePath + formattedDate + ".pdf";
        File newFile = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        fileOutputStream.write(file);
        fileOutputStream.close();
        return path;
    }

    public static class PdfFile {
        @FormParam("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] file;
    }

    private boolean isFileEmpty(PdfFile file) {
        return file.file == null || file.file.length == 0;
    }
}
