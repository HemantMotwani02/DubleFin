package com.dublefin.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String senderAccountNumber;
    
    @Column(nullable = false)
    private String receiverAccountNumber;
    
    @Column
    private String senderName;
    
    @Column
    private String receiverName;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(length = 1000)
    private String remarks;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    @Column
    private String referenceNumber;
    
    @Column
    private Double balanceAfterTransaction;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER
    }
    
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, REVERSED
    }
}

