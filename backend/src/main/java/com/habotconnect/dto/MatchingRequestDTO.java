package com.habotconnect.dto;

import com.habotconnect.entity.MatchingRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MatchingRequestDTO {

    public Long id;

    @NotNull(message = "studentId is required")
    public Long studentId;

    @NotNull(message = "parentId is required")
    public Long parentId;

    public String preferredSpecialization;
    public String status;
    public LocalDateTime createdAt;

    public static MatchingRequestDTO from(MatchingRequest r) {
        MatchingRequestDTO d = new MatchingRequestDTO();
        d.id = r.id;
        d.studentId = r.student != null ? r.student.id : null;
        d.parentId = r.parent != null ? r.parent.id : null;
        d.preferredSpecialization = r.preferredSpecialization;
        d.status = r.status;
        d.createdAt = r.createdAt;
        return d;
    }
}
