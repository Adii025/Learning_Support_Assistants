package com.habotconnect.service;

import com.habotconnect.dto.LsaProfileDTO;
import com.habotconnect.entity.LsaProfile;
import com.habotconnect.repository.LsaProfileRepository;
import com.habotconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LsaProfileService {

    private final LsaProfileRepository lsas;
    private final UserRepository users;
    private final AuditLogService audit;

    public LsaProfileService(LsaProfileRepository lsas, UserRepository users, AuditLogService audit) {
        this.lsas = lsas;
        this.users = users;
        this.audit = audit;
    }

    public List<LsaProfileDTO> list() {
        return lsas.findAll().stream().map(LsaProfileDTO::from).toList();
    }

    public LsaProfileDTO create(LsaProfileDTO dto) {
        LsaProfile x = new LsaProfile();
        apply(x, dto);
        LsaProfile saved = lsas.save(x);
        audit.record("CREATE_LSA_PROFILE", "LsaProfile", saved.id);
        return LsaProfileDTO.from(saved);
    }

    public LsaProfileDTO update(Long id, LsaProfileDTO dto) {
        LsaProfile x = lsas.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LSA profile not found: " + id));
        apply(x, dto);
        LsaProfile saved = lsas.save(x);
        audit.record("UPDATE_LSA_PROFILE", "LsaProfile", saved.id);
        return LsaProfileDTO.from(saved);
    }

    private void apply(LsaProfile x, LsaProfileDTO dto) {
        x.user = users.findById(dto.userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + dto.userId));
        x.specialization = dto.specialization;
        x.experienceYears = dto.experienceYears;
        x.location = dto.location;
        x.bio = dto.bio;
        x.active = dto.active == null ? true : dto.active;
    }
}
