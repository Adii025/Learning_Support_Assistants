package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    public String password;

    @Enumerated(EnumType.STRING)
    public Role role;

    public String name;

    public User() {
    }

    public User(String e, String p, String n, Role r) {
        email = e;
        password = p;
        name = n;
        role = r;
    }
}