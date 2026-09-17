package com.habotconnect.dto;

import com.habotconnect.entity.LsaProfile;
import jakarta.validation.constraints.NotNull;

public class LsaProfileDTO {

    public Long id;

    @NotNull(message = "userId is required")
    public Long userId;

    public String userName;
    public String specialization;
    public Integer experienceYears;
    public String location;
    public String bio;
    public Boolean active;

    public static LsaProfileDTO from(LsaProfile l) {
        LsaProfileDTO d = new LsaProfileDTO();
        d.id = l.id;
        d.userId = l.user != null ? l.user.id : null;
        d.userName = l.user != null ? l.user.name : null;
        d.specialization = l.specialization;
        d.experienceYears = l.experienceYears;
        d.location = l.location;
        d.bio = l.bio;
        d.active = l.active;
        return d;
    }
}
