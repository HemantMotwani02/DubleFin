package com.dublefin.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(unique = true)
    private String phone;
    
    @Column
    private LocalDate dateOfBirth;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(length = 1000)
    private String address;
    
    @Column
    private String city;
    
    @Column
    private String state;
    
    @Column
    private String country;
    
    @Column
    private String postalCode;
    
    @Column
    private String profilePictureUrl;
    
    @Column
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus = KycStatus.PENDING;
    
    @Column
    private String kycDocumentType;
    
    @Column
    private String kycDocumentNumber;
    
    @Column
    private String kycDocumentUrl;
    
    @Column
    private LocalDateTime kycVerifiedAt;
    
    @Column
    private String occupation;
    
    @Column
    private String employer;
    
    @Column
    private Double annualIncome;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum KycStatus {
        PENDING, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED
    }
}

