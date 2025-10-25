package com.dublefin.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String name;
    private String email;
    private String accountNumber;
    private Double balance;
    private String token;
    private String refreshToken;
    private Boolean twoFactorEnabled;
    private Boolean requiresTwoFactor;
}

