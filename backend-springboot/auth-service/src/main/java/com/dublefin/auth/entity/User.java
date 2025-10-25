package com.dublefin.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 11)
    private String accountNumber;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Double balance = 0.0;
    
    @Column(nullable = false)
    private Boolean enabled = true;
    
    @Column(nullable = false)
    private Boolean locked = false;
    
    @Column
    private Integer failedLoginAttempts = 0;
    
    @Column
    private LocalDateTime lockTime;
    
    @Column
    private LocalDateTime lastPasswordChange;
    
    @Column
    private Boolean twoFactorEnabled = false;
    
    @Column
    private String twoFactorSecret;
    
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING_VERIFICATION;
    
    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CUSTOMER;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum UserStatus {
        PENDING_VERIFICATION,
        ACTIVE,
        SUSPENDED,
        CLOSED
    }
    
    public enum UserRole {
        CUSTOMER,
        ADMIN,
        MANAGER
    }
}

