package com.habotconnect.service;

import com.habotconnect.dto.SessionDTO;
import com.habotconnect.entity.Session;
import com.habotconnect.repository.LsaProfileRepository;
import com.habotconnect.repository.SessionRepository;
import com.habotconnect.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SessionService {

    private final SessionRepository sessions;
    private final StudentRepository students;
    private final LsaProfileRepository lsas;
    private final AuditLogService audit;

    public SessionService(SessionRepository sessions, StudentRepository students,
                           LsaProfileRepository lsas, AuditLogService audit) {
        this.sessions = sessions;
        this.students = students;
        this.lsas = lsas;
        this.audit = audit;
    }

    public List<SessionDTO> list() {
        return sessions.findAll().stream().map(SessionDTO::from).toList();
    }

    public SessionDTO create(SessionDTO dto) {
        Session x = new Session();
        apply(x, dto);
        Session saved = sessions.save(x);
        audit.record("CREATE_SESSION", "Session", saved.id);
        return SessionDTO.from(saved);
    }

    public SessionDTO update(Long id, SessionDTO dto) {
        Session x = sessions.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Session not found: " + id));
        apply(x, dto);
        Session saved = sessions.save(x);
        audit.record("UPDATE_SESSION", "Session", saved.id);
        return SessionDTO.from(saved);
    }

    private void apply(Session x, SessionDTO dto) {
        x.student = students.findById(dto.studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + dto.studentId));
        x.lsa = lsas.findById(dto.lsaId)
                .orElseThrow(() -> new NoSuchElementException("LSA profile not found: " + dto.lsaId));
        x.scheduledAt = dto.scheduledAt;
        if (dto.status != null) x.status = dto.status;
        x.notes = dto.notes;
    }
}
