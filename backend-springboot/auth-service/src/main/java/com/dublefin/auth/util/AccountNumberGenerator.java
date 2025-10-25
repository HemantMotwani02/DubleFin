package com.dublefin.auth.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String PREFIX = "10"; // Bank prefix
    
    public String generateAccountNumber() {
        // Generate 9 random digits
        StringBuilder accountNumber = new StringBuilder(PREFIX);
        for (int i = 0; i < 9; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
}

