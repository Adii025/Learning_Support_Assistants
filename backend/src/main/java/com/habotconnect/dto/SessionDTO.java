package com.habotconnect.dto;

import com.habotconnect.entity.Session;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SessionDTO {

    public Long id;

    @NotNull(message = "studentId is required")
    public Long studentId;

    @NotNull(message = "lsaId is required")
    public Long lsaId;

    public LocalDateTime scheduledAt;
    public String status;
    public String notes;

    public static SessionDTO from(Session s) {
        SessionDTO d = new SessionDTO();
        d.id = s.id;
        d.studentId = s.student != null ? s.student.id : null;
        d.lsaId = s.lsa != null ? s.lsa.id : null;
        d.scheduledAt = s.scheduledAt;
        d.status = s.status;
        d.notes = s.notes;
        return d;
    }
}
