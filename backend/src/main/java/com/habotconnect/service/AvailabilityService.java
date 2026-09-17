package com.habotconnect.service;

import com.habotconnect.dto.AvailabilityDTO;
import com.habotconnect.entity.Availability;
import com.habotconnect.repository.AvailabilityRepository;
import com.habotconnect.repository.LsaProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availability;
    private final LsaProfileRepository lsas;
    private final AuditLogService audit;

    public AvailabilityService(AvailabilityRepository availability, LsaProfileRepository lsas, AuditLogService audit) {
        this.availability = availability;
        this.lsas = lsas;
        this.audit = audit;
    }

    public List<AvailabilityDTO> list() {
        return availability.findAll().stream().map(AvailabilityDTO::from).toList();
    }

    public AvailabilityDTO create(AvailabilityDTO dto) {
        Availability x = new Availability();
        x.lsa = lsas.findById(dto.lsaId)
                .orElseThrow(() -> new NoSuchElementException("LSA profile not found: " + dto.lsaId));
        x.dayOfWeek = dto.dayOfWeek;
        x.startTime = dto.startTime;
        x.endTime = dto.endTime;
        x.available = dto.available == null ? true : dto.available;

        Availability saved = availability.save(x);
        audit.record("CREATE_AVAILABILITY", "Availability", saved.id);
        return AvailabilityDTO.from(saved);
    }
}
