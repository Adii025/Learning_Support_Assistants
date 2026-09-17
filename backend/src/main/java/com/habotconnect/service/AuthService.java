package com.habotconnect.service;

import com.habotconnect.dto.AuthResponse;
import com.habotconnect.dto.LoginRequest;
import com.habotconnect.dto.MessageResponse;
import com.habotconnect.dto.RegisterRequest;
import com.habotconnect.entity.LsaProfile;
import com.habotconnect.entity.Parent;
import com.habotconnect.entity.Role;
import com.habotconnect.entity.User;
import com.habotconnect.repository.LsaProfileRepository;
import com.habotconnect.repository.ParentRepository;
import com.habotconnect.repository.UserRepository;
import com.habotconnect.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final ParentRepository parents;
    private final LsaProfileRepository lsaProfiles;
    private final BCryptPasswordEncoder enc;
    private final JwtUtil jwtUtil;
    private final AuditLogService audit;

    public AuthService(UserRepository repo, ParentRepository parents, LsaProfileRepository lsaProfiles,
                        BCryptPasswordEncoder enc, JwtUtil jwtUtil, AuditLogService audit) {
        this.repo = repo;
        this.parents = parents;
        this.lsaProfiles = lsaProfiles;
        this.enc = enc;
        this.jwtUtil = jwtUtil;
        this.audit = audit;
    }

    public MessageResponse register(RegisterRequest req) {
        if (repo.existsByEmailIgnoreCase(req.email)) {
            throw new RuntimeException("Email already registered");
        }

        User u = new User();
        u.email = req.email;
        u.password = enc.encode(req.password);
        u.name = req.name;
        u.role = req.role == null ? Role.PARENT : req.role;

        repo.save(u);

        // Students hang off a "parents" row (not directly off "users"), and
        // LSA profiles/availability/sessions hang off an "lsa_profiles" row.
        // Without creating these here, every downstream table stays empty
        // because their required foreign keys never exist.
        if (u.role == Role.PARENT) {
            Parent p = new Parent();
            p.user = u;
            parents.save(p);
        } else if (u.role == Role.LSA) {
            LsaProfile l = new LsaProfile();
            l.user = u;
            l.active = true;
            lsaProfiles.save(l);
        }

        audit.record("REGISTER", "User", u.id);

        return new MessageResponse("Registered successfully");
    }

    public AuthResponse login(LoginRequest req) {
        User u = repo.findByEmailIgnoreCase(req.email)
                .filter(x -> enc.matches(req.password, x.password))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        String token = jwtUtil.generateToken(u.email, u.role.name(), u.id);

        audit.record("LOGIN", "User", u.id);

        Long profileId = null;
        if (u.role == Role.PARENT) {
            profileId = parents.findByUser_Id(u.id).map(p -> p.id).orElse(null);
        } else if (u.role == Role.LSA) {
            profileId = lsaProfiles.findByUser_Id(u.id).map(l -> l.id).orElse(null);
        }

        return new AuthResponse(token, u.id, u.name, u.email, u.role, profileId);
    }
}
