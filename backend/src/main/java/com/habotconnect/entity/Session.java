package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    public Student student;

    @ManyToOne(optional = false)
    public LsaProfile lsa;

    public java.time.LocalDateTime scheduledAt;
    public String status = "SCHEDULED";

    @Column(length = 2000)
    public String notes;
}