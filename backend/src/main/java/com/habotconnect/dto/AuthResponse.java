package com.habotconnect.dto;

import com.habotconnect.entity.Role;

public class AuthResponse {

    public String token;
    public String tokenType = "Bearer";
    public Long userId;
    public String name;
    public String email;
    public Role role;

    // id of the parents/lsa_profiles row linked to this user (null for ADMIN,
    // or if that row somehow doesn't exist yet). The frontend needs this to
    // create students / lsa profile records against the right foreign key.
    public Long profileId;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long userId, String name, String email, Role role) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public AuthResponse(String token, Long userId, String name, String email, Role role, Long profileId) {
        this(token, userId, name, email, role);
        this.profileId = profileId;
    }
}
