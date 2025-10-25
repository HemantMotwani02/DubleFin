package com.dublefin.notification.controller;

import com.dublefin.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> request) {
        try {
            emailService.sendEmail(
                request.get("to"),
                request.get("subject"),
                request.get("body")
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email sent successfully");
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
        response.put("service", "notification-service");
        return ResponseEntity.ok(response);
    }
}

