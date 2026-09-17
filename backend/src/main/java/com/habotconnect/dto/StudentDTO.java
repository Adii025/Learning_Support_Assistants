package com.habotconnect.dto;

import com.habotconnect.entity.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentDTO {

    public Long id;

    @NotNull(message = "parentId is required")
    public Long parentId;

    @NotBlank(message = "Name is required")
    public String name;

    public Integer age;
    public String grade;
    public String learningDifficulty;
    public String location;
    public String notes;
    public Long predecessorId;

    public static StudentDTO from(Student s) {
        StudentDTO d = new StudentDTO();
        d.id = s.id;
        d.parentId = s.parent != null ? s.parent.id : null;
        d.name = s.name;
        d.age = s.age;
        d.grade = s.grade;
        d.learningDifficulty = s.learningDifficulty;
        d.location = s.location;
        d.notes = s.notes;
        d.predecessorId = s.predecessor != null ? s.predecessor.id : null;
        return d;
    }
}
