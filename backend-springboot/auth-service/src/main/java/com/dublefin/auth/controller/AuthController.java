package com.dublefin.auth.controller;

import com.dublefin.auth.dto.*;
import com.dublefin.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            if (response.getRequiresTwoFactor() != null && response.getRequiresTwoFactor()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<?> setupTwoFactor(@RequestHeader("X-User-Id") Long userId) {
        try {
            TwoFactorResponse response = authService.setupTwoFactor(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<?> enableTwoFactor(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        try {
            authService.enableTwoFactor(userId, request.get("code"));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Two-factor authentication enabled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disableTwoFactor(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        try {
            authService.disableTwoFactor(userId, request.get("code"));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Two-factor authentication disabled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "auth-service");
        return ResponseEntity.ok(response);
    }
}

