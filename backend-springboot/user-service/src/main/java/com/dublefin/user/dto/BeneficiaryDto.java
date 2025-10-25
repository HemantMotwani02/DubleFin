package com.dublefin.user.dto;

import com.dublefin.user.entity.Beneficiary;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    private String bankName;
    private String ifscCode;
    private String nickname;
    private Beneficiary.BeneficiaryType type;
    private Boolean isActive;
}

