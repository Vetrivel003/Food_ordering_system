package com.project.sapaadu.controller;

import com.project.sapaadu.dto.request.LoginRequest;
import com.project.sapaadu.dto.request.RefreshRequest;
import com.project.sapaadu.dto.request.RegisterRequest;
import com.project.sapaadu.dto.response.LoginResponse;
import com.project.sapaadu.dto.response.RefreshResponse;
import com.project.sapaadu.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
            @Valid @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                userService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshRequest request) {

        userService.logout(request.getRefreshToken());

        return ResponseEntity.ok("Logged out successfully");
    }
}
