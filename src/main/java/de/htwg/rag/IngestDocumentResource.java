package de.htwg.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.PartType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Path("/ingest")
@ApplicationScoped
public class IngestDocumentResource {

    @Inject
    DocumentIngestor documentIngestor;

    private static final String UPLOAD_DIRECTORY = "resources/pdfs";

    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadPdf(@MultipartForm PdfFile pdfFile, @FormParam("name") String name) {
        try {
            // try to create a new file and safe the pdf to the UPLOAD_DIRECTORY
            String filePath = UPLOAD_DIRECTORY + "/" + name + ".pdf";
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (pdfFile.file == null) throw new RuntimeException("File is null");
            fileOutputStream.write(pdfFile.file);
            fileOutputStream.close();

            // load the document with the documentParser and ingest it as a list, mabye add the possibility to send multiple files at once
            Document document = FileSystemDocumentLoader.loadDocument(filePath, new ApachePdfBoxDocumentParser());
            documentIngestor.ingest(List.of(document));

        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    public static class PdfFile {
        @FormParam("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] file;
    }
}
