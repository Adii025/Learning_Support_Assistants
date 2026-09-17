package com.habotconnect.dto;

import com.habotconnect.entity.Match;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MatchDTO {

    public Long id;

    @NotNull(message = "studentId is required")
    public Long studentId;

    @NotNull(message = "lsaId is required")
    public Long lsaId;

    @NotNull(message = "requestId is required")
    public Long requestId;

    public String status;
    public Integer score;
    public LocalDateTime matchedAt;

    public static MatchDTO from(Match m) {
        MatchDTO d = new MatchDTO();
        d.id = m.id;
        d.studentId = m.student != null ? m.student.id : null;
        d.lsaId = m.lsa != null ? m.lsa.id : null;
        d.requestId = m.request != null ? m.request.id : null;
        d.status = m.status;
        d.score = m.score;
        d.matchedAt = m.matchedAt;
        return d;
    }
}
