package com.dublefin.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Long userId;
    
    @Column(nullable = false)
    private String action;
    
    @Column
    private String details;
    
    @Column
    private String ipAddress;
    
    @Column
    private String userAgent;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionStatus status;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;
    
    public enum ActionStatus {
        SUCCESS,
        FAILURE,
        PENDING
    }
}

