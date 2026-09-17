package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id")
    public Parent parent;

    @Column(nullable = false)
    public String name;

    public Integer age;
    public String grade;
    public String learningDifficulty;
    public String location;
    public String notes;

    @ManyToOne
    @JoinColumn(name = "predecessor_id")
    public Student predecessor;
}