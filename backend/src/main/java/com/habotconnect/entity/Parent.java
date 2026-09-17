package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    public User user;

    public String phone;
    public String address;
}