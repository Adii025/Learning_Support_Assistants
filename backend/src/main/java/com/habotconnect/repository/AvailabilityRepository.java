package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}