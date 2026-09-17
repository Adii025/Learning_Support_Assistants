package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.MatchingRequest;

public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {
}