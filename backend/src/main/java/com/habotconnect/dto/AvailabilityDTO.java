package com.habotconnect.dto;

import com.habotconnect.entity.Availability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AvailabilityDTO {

    public Long id;

    @NotNull(message = "lsaId is required")
    public Long lsaId;

    @NotBlank(message = "dayOfWeek is required")
    public String dayOfWeek;

    public String startTime;
    public String endTime;
    public Boolean available;

    public static AvailabilityDTO from(Availability a) {
        AvailabilityDTO d = new AvailabilityDTO();
        d.id = a.id;
        d.lsaId = a.lsa != null ? a.lsa.id : null;
        d.dayOfWeek = a.dayOfWeek;
        d.startTime = a.startTime;
        d.endTime = a.endTime;
        d.available = a.available;
        return d;
    }
}
