package de.htwg.rag;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class IngestDocumentResourceTest {

    @InjectMock
    DocumentIngestor documentIngestor;

    @Inject
    UploadFileRepository uploadFileRepository;


    @Test
    @TestTransaction
    void testUploadPdfForValidInput() {
        doNothing().when(documentIngestor).ingest(anyList());
        String randowmName = UUID.randomUUID().toString();
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

}