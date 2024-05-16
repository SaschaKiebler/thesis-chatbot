package de.htwg.multipleChoice.repositories;


import de.htwg.multipleChoice.entities.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

    public Student findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void deleteLecture(UUID studentId, UUID lectureId) {
        Student student = findById(studentId);
        student.getLectures().removeIf(lecture -> lecture.getId().equals(lectureId));
        persist(student);
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }
}
