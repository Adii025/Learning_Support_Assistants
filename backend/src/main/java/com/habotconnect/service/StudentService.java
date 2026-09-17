package com.habotconnect.service;

import com.habotconnect.dto.StudentDTO;
import com.habotconnect.entity.Student;
import com.habotconnect.repository.ParentRepository;
import com.habotconnect.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentRepository students;
    private final ParentRepository parents;
    private final AuditLogService audit;

    public StudentService(StudentRepository students, ParentRepository parents, AuditLogService audit) {
        this.students = students;
        this.parents = parents;
        this.audit = audit;
    }

    public List<StudentDTO> list() {
        return students.findAll().stream().map(StudentDTO::from).toList();
    }

    public StudentDTO create(StudentDTO dto) {
        Student x = new Student();
        apply(x, dto);
        Student saved = students.save(x);
        audit.record("CREATE_STUDENT", "Student", saved.id);
        return StudentDTO.from(saved);
    }

    public StudentDTO update(Long id, StudentDTO dto) {
        Student x = students.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
        apply(x, dto);
        Student saved = students.save(x);
        audit.record("UPDATE_STUDENT", "Student", saved.id);
        return StudentDTO.from(saved);
    }

    public void delete(Long id) {
        if (!students.existsById(id)) {
            throw new NoSuchElementException("Student not found: " + id);
        }
        students.deleteById(id);
        audit.record("DELETE_STUDENT", "Student", id);
    }

    private void apply(Student x, StudentDTO dto) {
        x.parent = parents.findById(dto.parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found: " + dto.parentId));
        x.name = dto.name;
        x.age = dto.age;
        x.grade = dto.grade;
        x.learningDifficulty = dto.learningDifficulty;
        x.location = dto.location;
        x.notes = dto.notes;
        x.predecessor = dto.predecessorId == null ? null :
                students.findById(dto.predecessorId)
                        .orElseThrow(() -> new NoSuchElementException("Predecessor student not found: " + dto.predecessorId));
    }
}
