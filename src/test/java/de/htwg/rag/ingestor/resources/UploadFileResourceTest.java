package de.htwg.rag.ingestor.resources;

import de.htwg.rag.ingestor.UploadFileRepository;
import de.htwg.rag.ingestor.UploadedFile;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class UploadFileResourceTest {

    @InjectMock
    UploadFileRepository uploadFileRepository;

    @Test
    void downloadFile() {
        UploadedFile uploadedFile = UploadedFile.builder().id(UUID.randomUUID()).name("Krankenhausfinanzierung.pdf").path("src/test/resources/pdfs/Krankenhausfinanzierung.pdf").build();
        when(uploadFileRepository.findById(any(UUID.class))).thenReturn(uploadedFile);
        given()
                .when().get("/api/files/" + uploadedFile.getId())
                .then()
                .statusCode(200);

    }

    @Test
    void deleteFile() {
        UUID id = UUID.randomUUID();
        when(uploadFileRepository.deleteById(any(UUID.class))).thenReturn(true);
        given()
                .when().delete("/api/files/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    void getAllFiles() {
        when(uploadFileRepository.listAll()).thenReturn(null);
        given()
                .when().get("/api/files/all")
                .then()
                .statusCode(200);
    }

    @Test
    void downloadFileNotFound() {
        UUID id = UUID.randomUUID();
        when(uploadFileRepository.findById(any(UUID.class))).thenReturn(null);
        given()
                .when().get("/api/files/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void deleteFileNotFound() {
        UUID id = UUID.randomUUID();
        when(uploadFileRepository.deleteById(any(UUID.class))).thenThrow(new RuntimeException());
        given()
                .when().delete("/api/files/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void getAllFilesNotFound() {
        when(uploadFileRepository.listAll()).thenReturn(List.of());
        given()
                .when().get("/api/files/all")
                .then()
                .statusCode(200)
                .body(
                "size()", equalTo(0)
                );
    }
}