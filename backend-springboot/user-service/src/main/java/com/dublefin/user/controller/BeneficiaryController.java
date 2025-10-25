package com.dublefin.user.controller;

import com.dublefin.user.dto.BeneficiaryDto;
import com.dublefin.user.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/beneficiaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    public ResponseEntity<?> getAllBeneficiaries(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<BeneficiaryDto> beneficiaries = beneficiaryService.getAllBeneficiaries(userId);
            return ResponseEntity.ok(beneficiaries);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> addBeneficiary(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BeneficiaryDto beneficiaryDto) {
        try {
            BeneficiaryDto beneficiary = beneficiaryService.addBeneficiary(userId, beneficiaryDto);
            return ResponseEntity.ok(beneficiary);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<?> deleteBeneficiary(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long beneficiaryId) {
        try {
            beneficiaryService.deleteBeneficiary(userId, beneficiaryId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Beneficiary deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}

