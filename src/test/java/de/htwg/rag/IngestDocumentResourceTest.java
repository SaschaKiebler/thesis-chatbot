package de.htwg.rag;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class IngestDocumentResourceTest {

    @InjectMock
    DocumentIngestor documentIngestor;

    IngestDocumentResource resource;

    @BeforeEach
    void setUp() {
        resource = new IngestDocumentResource();
        resource.documentIngestor = documentIngestor;
    }


    @Test
    void testUploadPdfForValidInput() {
        doNothing().when(documentIngestor).ingest(anyList());
        given()
                .when().formParam("name","test").multiPart("file", new File("src/test/resources/pdfs/Krankenhausfinanzierung.pdf"))
                .post("/ingest/pdf")
                .then()
                .statusCode(204);

        verify(documentIngestor, times(1)).ingest(anyList());
    }

    @Test
    void testUploadPdfForInvalidInput() {
        doThrow(new RuntimeException("Error saving file")).when(documentIngestor).ingest(anyList());
        given()
                .when().formParam("name","test").multiPart("file", new File("src/test/resources/pdfs/Krankenhausfinanzierung.pdf"))
                .post("/ingest/pdf")
                .then()
                .statusCode(500);

        verify(documentIngestor, times(1)).ingest(anyList());
    }

    @Test
    void testUploadPdfForNullFile() {
        doThrow(new RuntimeException("File is null")).when(documentIngestor).ingest(anyList());
        given()
                .when().formParam("name","test").multiPart("file", (InputStream) null)
                .post("/ingest/pdf")
                .then()
                .statusCode(500);

        verify(documentIngestor, times(0)).ingest(anyList());
    }
}