package de.htwg.rag.ingestor.resources;

import de.htwg.multipleChoice.entities.Lecture;
import de.htwg.multipleChoice.entities.Script;
import de.htwg.multipleChoice.repositories.LectureRepository;
import de.htwg.multipleChoice.repositories.ScriptRepository;
import de.htwg.rag.dataTools.Summarizer;
import de.htwg.rag.dataTools.WebsiteTextExtractor;
import de.htwg.rag.ingestor.DocumentIngestor;
import de.htwg.rag.ingestor.UploadFileRepository;
import de.htwg.rag.ingestor.UploadedFile;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
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
import java.util.Date;
import java.util.List;

/**
 * This class is the Resource for the Ingestion of Documents.
 * It serves the Ingestion of Documents via POST requests.
 * It can ingest PDFs and URLs.
 */
@Path("/api/ingest")
@ApplicationScoped
public class IngestDocumentResource {

    @Inject
    DocumentIngestor documentIngestor;

    @Inject
    UploadFileRepository uploadFileRepository;

    @Inject
    LectureRepository lectureRepository;

    @Inject
    ScriptRepository scriptRepository;

    @Inject
    Summarizer summarizer;

    @Inject
    WebsiteTextExtractor websiteTextExtractor;

    private static final String UPLOAD_DIRECTORY = "resources/pdfs";

    /**
     * This method is called when a POST request is sent to /pdf.
     * It ingests a PDF file and safes the Embeddings, the summarized text and a Object of the UploadedFile class.
     * @param pdfFile the PDF file to ingest
     * @param name the name of the PDF file
     * @return a Response
     */
    @POST
    @Path("/pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    @TransactionConfiguration(timeout = 3000)
    public Response uploadPdf(@MultipartForm PdfFile pdfFile, @FormParam("name") String name, @FormParam("lecture") String lectureName) {
        if (isFileEmpty(pdfFile)) {
            System.err.println("Uploaded File is empty");
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

            // load the document with the documentParser and add the fileKey to the metadata
            Document document = FileSystemDocumentLoader.loadDocument(path, new ApachePdfBoxDocumentParser());
            // save the link to the file in Markdown syntax in the metadata  better understanding for the llm
            document.metadata().add("link","["+ uploadedFile.getName() +"](/api/files/" + uploadedFile.getId().toString() + ")");
            document.metadata().add("fileKey", uploadedFile.getId().toString());

            // Safe the Script with plain text and add the lecture
            if (!lectureName.isEmpty()) {
            String text = document.text();
            Lecture lecture = lectureRepository.findByName(lectureName);
            if (lecture == null) {
                lecture = Lecture.builder().name(lectureName).build();
                lectureRepository.persist(lecture);
            }
            scriptRepository.persist(Script.builder().name(name).lecture(lecture).text(text).build());
            }

            // Summarize the text and ingest it
            String sumtext = summarizer.summarize(document.text());
            document = Document.document(sumtext, document.metadata());

            documentIngestor.ingest(List.of(document));
            System.out.println("Ingested file with id: " + uploadedFile.getId() + " at " + new Date());
            return Response.ok().build();

        } catch (Exception e) {
            try {
                Files.deleteIfExists(java.nio.file.Path.of(path));
            } catch (IOException ex) {
                System.err.println("Error deleting file after exception");
            }
            System.err.println("Error ingesting file: " + e);
            throw new RuntimeException("Error saving file", e);
        }
    }

    /**
     * This method is used to save a file to the file system.
     * It returns the path of the saved file.
     *
     * @param filePath the path to save the file
     * @param file the file to save
     * @return the path of the saved file
     */
    private String safeFile(String filePath, byte[] file) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String formattedDate = formatter.format(new Date());
        String path = filePath + formattedDate + ".pdf";
        File newFile = new File(path);
        // important to create the parent directories if they don't exist
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        fileOutputStream.write(file);
        fileOutputStream.close();
        return path;
    }

    /**
     * This class is used to represent a PDF file.
     */
    public static class PdfFile {
        @FormParam("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] file;
    }

    /**
     * This method is used to check if a file is empty.
     * It returns true if the file is empty.
     *
     * @param file the file to check
     * @return true if the file is empty
     */
    private boolean isFileEmpty(PdfFile file) {
        return file.file == null || file.file.length == 0;
    }

    /**
     * This method is called when a POST request is sent to /url. impl not finished
     * It ingests a URL.
     * @param url the URL to ingest
     * @param name the name of the URL
     * @return a Response
     */
    @POST
    @Path("/url")
    @Transactional
    @TransactionConfiguration(timeout = 3000)
    public Response ingestUrl(@FormParam("url") String url, @FormParam("name") String name){
        try {
            Document document = UrlDocumentLoader.load(url, new TextDocumentParser());
            // remove the html tags from the text
            document = Document.document(document.text().replaceAll("<[^>]*>", ""), document.metadata());
            // clean the text from special characters and js
            String text = websiteTextExtractor.extractText(document.text());
            document = Document.document(text, document.metadata());
            documentIngestor.ingest(List.of(document));
            UploadedFile uploadedFile = new UploadedFile(name, url);
            uploadFileRepository.persist(uploadedFile);
            System.out.println("Ingested url: " + url + " at " + new Date());
            return Response.ok().build();
        } catch (Exception e) {
            System.err.println("Error ingesting url: " + e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Error ingesting url").build();
        }
    }
}
