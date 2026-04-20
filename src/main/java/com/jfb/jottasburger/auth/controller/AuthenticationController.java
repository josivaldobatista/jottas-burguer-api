package com.jfb.jottasburger.auth.controller;

import com.jfb.jottasburger.auth.dto.LoginRequest;
import com.jfb.jottasburger.auth.dto.LoginResponse;
import com.jfb.jottasburger.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}