package com.dublefin.transaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "account-service")
public interface AccountServiceClient {
    
    @GetMapping("/api/accounts/{accountNumber}")
    Map<String, Object> getAccount(@PathVariable("accountNumber") String accountNumber);
    
    @PutMapping("/api/accounts/{accountNumber}/balance")
    Map<String, Object> updateBalance(
            @PathVariable("accountNumber") String accountNumber,
            @RequestBody Map<String, Object> request);
}

