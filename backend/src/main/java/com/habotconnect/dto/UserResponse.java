package com.habotconnect.dto;

import com.habotconnect.entity.Role;
import com.habotconnect.entity.User;

public class UserResponse {

    public Long id;
    public String name;
    public String email;
    public Role role;

    public static UserResponse from(User u) {
        UserResponse r = new UserResponse();
        r.id = u.id;
        r.name = u.name;
        r.email = u.email;
        r.role = u.role;
        return r;
    }
}
