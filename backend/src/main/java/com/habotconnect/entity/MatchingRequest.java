package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matching_requests")
public class MatchingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    public Student student;

    @ManyToOne(optional = false)
    public Parent parent;

    public String preferredSpecialization;
    public String status = "PENDING";

    public java.time.LocalDateTime createdAt =
            java.time.LocalDateTime.now();
}