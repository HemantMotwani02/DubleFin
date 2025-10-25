package com.dublefin.account.controller;

import com.dublefin.account.entity.Account;
import com.dublefin.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<?> getUserAccounts(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<Account> accounts = accountService.getUserAccounts(userId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Object> request) {
        try {
            String accountNumber = (String) request.get("accountNumber");
            Account.AccountType accountType = Account.AccountType.valueOf(
                    (String) request.getOrDefault("accountType", "SAVINGS"));
            
            Account account = accountService.createAccount(userId, accountNumber, accountType);
            return ResponseEntity.ok(account);
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
        response.put("service", "account-service");
        return ResponseEntity.ok(response);
    }
}

