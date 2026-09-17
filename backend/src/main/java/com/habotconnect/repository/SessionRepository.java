package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}