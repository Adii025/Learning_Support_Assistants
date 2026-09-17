package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
}