package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(optional = false)
    public Student student;

    @ManyToOne(optional = false)
    public LsaProfile lsa;

    @OneToOne(optional = false)
    public MatchingRequest request;

    public String status = "ACTIVE";
    public Integer score;

    public java.time.LocalDateTime matchedAt =
            java.time.LocalDateTime.now();
}