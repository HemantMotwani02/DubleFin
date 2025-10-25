package com.dublefin.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorResponse {
    private String secret;
    private String qrCodeImage;
    private String manualEntryKey;
}

