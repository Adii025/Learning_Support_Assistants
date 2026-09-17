package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}