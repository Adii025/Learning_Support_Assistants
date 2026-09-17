package com.habotconnect.service;

import com.habotconnect.dto.MatchingRequestDTO;
import com.habotconnect.entity.MatchingRequest;
import com.habotconnect.repository.MatchingRequestRepository;
import com.habotconnect.repository.ParentRepository;
import com.habotconnect.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MatchingRequestService {

    private final MatchingRequestRepository requests;
    private final StudentRepository students;
    private final ParentRepository parents;
    private final AuditLogService audit;

    public MatchingRequestService(MatchingRequestRepository requests, StudentRepository students,
                                   ParentRepository parents, AuditLogService audit) {
        this.requests = requests;
        this.students = students;
        this.parents = parents;
        this.audit = audit;
    }

    public List<MatchingRequestDTO> list() {
        return requests.findAll().stream().map(MatchingRequestDTO::from).toList();
    }

    public MatchingRequestDTO create(MatchingRequestDTO dto) {
        MatchingRequest x = new MatchingRequest();
        x.student = students.findById(dto.studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + dto.studentId));
        x.parent = parents.findById(dto.parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found: " + dto.parentId));
        x.preferredSpecialization = dto.preferredSpecialization;
        if (dto.status != null) x.status = dto.status;

        MatchingRequest saved = requests.save(x);
        audit.record("CREATE_MATCH_REQUEST", "MatchingRequest", saved.id);
        return MatchingRequestDTO.from(saved);
    }

    public MatchingRequestDTO update(Long id, MatchingRequestDTO dto) {
        MatchingRequest x = requests.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Match request not found: " + id));
        x.preferredSpecialization = dto.preferredSpecialization;
        if (dto.status != null) x.status = dto.status;

        MatchingRequest saved = requests.save(x);
        audit.record("UPDATE_MATCH_REQUEST", "MatchingRequest", saved.id);
        return MatchingRequestDTO.from(saved);
    }
}
