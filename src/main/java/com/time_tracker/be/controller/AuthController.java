package com.time_tracker.be.controller;

import com.time_tracker.be.model.LoginRequest;
import com.time_tracker.be.model.RegisterRequest;
import com.time_tracker.be.model.ResponseModel;
import com.time_tracker.be.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/login-by-google")
    public ResponseEntity<ResponseModel<String>> loginWithGoogle(@RequestBody LoginRequest loginRequest) {
        return authService.loginWithGoogle(loginRequest.getIdToken());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseModel<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getName());
    }

}
