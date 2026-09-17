package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lsa_profiles")
public class LsaProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    public User user;

    public String specialization;
    public Integer experienceYears;
    public String location;
    public String bio;
    public Boolean active = true;
}