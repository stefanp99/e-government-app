package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.models.Role;
import com.stefan.egovernmentapp.models.requests.LoginRequest;
import com.stefan.egovernmentapp.models.requests.RegisterRequest;
import com.stefan.egovernmentapp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("register-resident")
    public ResponseEntity<String> registerResident(@RequestBody RegisterRequest registerRequest) {
        registerRequest.setRole(Role.RESIDENT);
        return authService.registerUser(registerRequest);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @PutMapping("2fa")
    public ResponseEntity<String> add2FA(@RequestHeader("Authorization") String token) {
        return authService.add2FA(token);
    }

    @DeleteMapping("2fa")
    public ResponseEntity<String> remove2FA(@RequestHeader("Authorization") String token) {
        return authService.remove2FA(token);
    }
}
