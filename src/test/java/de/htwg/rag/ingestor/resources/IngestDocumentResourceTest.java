package de.htwg.rag.ingestor.resources;

import de.htwg.rag.dataTools.Summarizer;
import de.htwg.rag.ingestor.DocumentIngestor;
import de.htwg.rag.ingestor.UploadFileRepository;
import de.htwg.rag.ingestor.UploadedFile;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class IngestDocumentResourceTest {

    @InjectMock
    DocumentIngestor documentIngestor;

    @Inject
    UploadFileRepository uploadFileRepository;

    @InjectMock
    Summarizer summarizer;


    @Test
    @TestTransaction
    void testUploadPdfForValidInput() {
        doNothing().when(documentIngestor).ingest(anyList());
        String randowmName = UUID.randomUUID().toString();
        when(summarizer.summarize(anyString())).thenReturn("summary");
       given()
                .when().formParam("name",randowmName).multiPart("file", new File("src/test/resources/pdfs/Krankenhausfinanzierung.pdf"))
                .post("/api/ingest/pdf")
                .then()
                .statusCode(200);

        verify(documentIngestor, times(1)).ingest(anyList());

        // cleanup
        UploadedFile file = uploadFileRepository.findByName(randowmName);

        given()
                .when().delete("/api/files/" + file.getId())
                .then()
                .statusCode(200);

    }

    @Test
    @TestTransaction
    void testUploadPdfForExceptionFromDocumentIngestor() {
        doThrow(new RuntimeException("Error saving file")).when(documentIngestor).ingest(anyList());
        when(summarizer.summarize(anyString())).thenReturn("summary");
        given()
                .when().formParam("name","test").multiPart("file", new File("src/test/resources/pdfs/Krankenhausfinanzierung.pdf"))
                .post("/api/ingest/pdf")
                .then()
                .statusCode(500);

        verify(documentIngestor, times(1)).ingest(anyList());
    }

    @Test
    void testUploadPdfForEmptyFile() {
        File emptyFile = new File("src/test/resources/pdfs/empty.pdf");

        given()
                .when().formParam("name","empty")
                .multiPart("file", emptyFile)
                .post("/api/ingest/pdf")
                .then()
                .statusCode(400);

        verify(documentIngestor, times(0)).ingest(anyList());
    }

    @Test
    void testUploadPdfForFileWithoutText() {
        File fileWithoutText = new File("src/test/resources/pdfs/PDFWithoutText.pdf");

        given()
                .when().formParam("name","test").multiPart("file", fileWithoutText)
                .post("/api/ingest/pdf")
                .then()
                .statusCode(500);

    }

}