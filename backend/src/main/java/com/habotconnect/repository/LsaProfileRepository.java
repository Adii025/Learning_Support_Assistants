package com.habotconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habotconnect.entity.LsaProfile;
import java.util.Optional;

public interface LsaProfileRepository extends JpaRepository<LsaProfile, Long> {
    Optional<LsaProfile> findByUser_Id(Long userId);
}