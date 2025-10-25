package com.dublefin.user.controller;

import com.dublefin.user.dto.KycSubmissionDto;
import com.dublefin.user.dto.UserProfileDto;
import com.dublefin.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("X-User-Id") Long userId) {
        try {
            UserProfileDto profile = userProfileService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserProfileDto profileDto) {
        try {
            UserProfileDto profile = userProfileService.createProfile(
                    userId, profileDto.getName(), profileDto.getEmail());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserProfileDto profileDto) {
        try {
            UserProfileDto profile = userProfileService.updateProfile(userId, profileDto);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/kyc/submit")
    public ResponseEntity<?> submitKyc(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody KycSubmissionDto kycDto) {
        try {
            UserProfileDto profile = userProfileService.submitKyc(userId, kycDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "KYC submitted successfully");
            response.put("profile", profile);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/kyc/status")
    public ResponseEntity<?> getKycStatus(@RequestHeader("X-User-Id") Long userId) {
        try {
            UserProfileDto profile = userProfileService.getUserProfile(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("kycStatus", profile.getKycStatus());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/account_details")
    public ResponseEntity<?> getAccountDetails(@RequestHeader("X-User-Id") Long userId) {
        try {
            UserProfileDto profile = userProfileService.getUserProfile(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("name", profile.getName());
            response.put("email", profile.getEmail());
            // Note: Balance would typically come from Account Service
            response.put("balance", 0.0);
            response.put("account_number", ""); // Would come from Account Service
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
        response.put("service", "user-service");
        return ResponseEntity.ok(response);
    }
}

