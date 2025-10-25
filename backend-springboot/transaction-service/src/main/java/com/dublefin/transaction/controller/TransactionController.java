package com.dublefin.transaction.controller;

import com.dublefin.transaction.dto.TransferRequest;
import com.dublefin.transaction.entity.Transaction;
import com.dublefin.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/send")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransferRequest request) {
        try {
            Transaction transaction = transactionService.processTransfer(request);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Transaction successful");
            response.put("referenceNumber", transaction.getReferenceNumber());
            response.put("transaction", transaction);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Transaction> transactions = transactionService.getTransactionHistory(
                    accountNumber, page, size);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTransactions(@RequestParam String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getRecentTransactions(accountNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/statement")
    public ResponseEntity<?> getStatement(
            @RequestParam String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByDateRange(
                    accountNumber, startDate, endDate);
            return ResponseEntity.ok(transactions);
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
        response.put("service", "transaction-service");
        return ResponseEntity.ok(response);
    }
}

