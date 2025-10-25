package com.dublefin.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String accountNumber;
    
    @Column
    private String bankName;
    
    @Column
    private String ifscCode;
    
    @Column
    private String nickname;
    
    @Column
    @Enumerated(EnumType.STRING)
    private BeneficiaryType type = BeneficiaryType.INTERNAL;
    
    @Column
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum BeneficiaryType {
        INTERNAL, EXTERNAL
    }
}

