package de.htwg.rag;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UploadFileRepository implements PanacheRepositoryBase<UploadedFile, UUID>{

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

    public List<UploadedFile> listAll() {
        return UploadedFile.listAll();
    }

}
