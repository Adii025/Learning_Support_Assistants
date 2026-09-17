package com.habotconnect.controller;

import com.habotconnect.dto.AuthResponse;
import com.habotconnect.dto.LoginRequest;
import com.habotconnect.dto.MessageResponse;
import com.habotconnect.dto.RegisterRequest;
import com.habotconnect.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public MessageResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
