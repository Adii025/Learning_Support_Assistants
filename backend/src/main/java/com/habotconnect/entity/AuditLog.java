package com.habotconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public String action;
    public String entityName;
    public Long entityId;

    public java.time.LocalDateTime createdAt =
            java.time.LocalDateTime.now();
}