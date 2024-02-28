package de.htwg.rag;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UploadFileRepository implements PanacheRepository<UploadedFile> {
}
