package com.habotconnect.service;

import com.habotconnect.dto.MatchDTO;
import com.habotconnect.entity.Match;
import com.habotconnect.repository.LsaProfileRepository;
import com.habotconnect.repository.MatchRepository;
import com.habotconnect.repository.MatchingRequestRepository;
import com.habotconnect.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MatchService {

    private final MatchRepository matches;
    private final StudentRepository students;
    private final LsaProfileRepository lsas;
    private final MatchingRequestRepository requests;
    private final AuditLogService audit;

    public MatchService(MatchRepository matches, StudentRepository students, LsaProfileRepository lsas,
                         MatchingRequestRepository requests, AuditLogService audit) {
        this.matches = matches;
        this.students = students;
        this.lsas = lsas;
        this.requests = requests;
        this.audit = audit;
    }

    public List<MatchDTO> list() {
        return matches.findAll().stream().map(MatchDTO::from).toList();
    }

    public MatchDTO create(MatchDTO dto) {
        Match x = new Match();
        x.student = students.findById(dto.studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + dto.studentId));
        x.lsa = lsas.findById(dto.lsaId)
                .orElseThrow(() -> new NoSuchElementException("LSA profile not found: " + dto.lsaId));
        x.request = requests.findById(dto.requestId)
                .orElseThrow(() -> new NoSuchElementException("Match request not found: " + dto.requestId));
        x.status = "ACTIVE";
        x.score = dto.score;

        Match saved = matches.save(x);
        audit.record("CREATE_MATCH", "Match", saved.id);
        return MatchDTO.from(saved);
    }
}
