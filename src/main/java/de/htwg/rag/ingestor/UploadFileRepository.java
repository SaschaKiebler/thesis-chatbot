package de.htwg.rag.ingestor;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * This class is the Repository for the Uploaded Files.
 */
@ApplicationScoped
public class UploadFileRepository implements PanacheRepositoryBase<UploadedFile, UUID>{

    /**
     * This method deletes a file by id.
     * @param id The id of the file to delete.
     * @return True if the file was deleted, false otherwise.
     */
    @Override
    public boolean deleteById(UUID id) {
        try{
        UploadedFile file = UploadedFile.findById(id);
        Files.deleteIfExists(Path.of(file.getPath()));
        file.delete();
        return true;
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }

    /**
     * This method lists all files in the database.
     * @return All files in the database.
     */
    public List<UploadedFile> listAll() {
        return UploadedFile.listAll();
    }

    /**
     * This method finds a file by name.
     * @param name The name of the file to find.
     * @return The file with the given name.
     */
    public UploadedFile findByName(String name) {
        return UploadedFile.find("name", name).firstResult();
    }

}
