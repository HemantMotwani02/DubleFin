package com.dublefin.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String accountNumber;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.SAVINGS;
    
    @Column(nullable = false)
    private Double balance = 0.0;
    
    @Column
    private Double interestRate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT, LOAN
    }
    
    public enum AccountStatus {
        ACTIVE, SUSPENDED, CLOSED
    }
}

