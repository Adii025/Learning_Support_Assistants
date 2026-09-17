package com.habotconnect.dto;

import com.habotconnect.entity.Parent;
import jakarta.validation.constraints.NotNull;

public class ParentDTO {

    public Long id;

    @NotNull(message = "userId is required")
    public Long userId;

    public String userName;
    public String phone;
    public String address;

    public static ParentDTO from(Parent p) {
        ParentDTO d = new ParentDTO();
        d.id = p.id;
        d.userId = p.user != null ? p.user.id : null;
        d.userName = p.user != null ? p.user.name : null;
        d.phone = p.phone;
        d.address = p.address;
        return d;
    }
}
