package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.LoginRequestDto;
import com.stefan.egovernmentapp.dtos.requests.RegisterRequestDto;
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
    public ResponseEntity<String> registerResident(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.registerUser(registerRequestDto, true);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.registerUser(registerRequestDto, false);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.loginUser(loginRequestDto);
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
