package com.dublefin.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycSubmissionDto {
    
    @NotBlank(message = "Document type is required")
    private String documentType;
    
    @NotBlank(message = "Document number is required")
    private String documentNumber;
    
    @NotBlank(message = "Document URL is required")
    private String documentUrl;
}

