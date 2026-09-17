package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}