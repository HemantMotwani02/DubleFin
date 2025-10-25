package com.dublefin.transaction.repository;

import com.dublefin.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    @Query("SELECT t FROM Transaction t WHERE t.senderAccountNumber = ?1 OR t.receiverAccountNumber = ?1 ORDER BY t.createdAt DESC")
    Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE (t.senderAccountNumber = ?1 OR t.receiverAccountNumber = ?1) " +
           "AND t.createdAt BETWEEN ?2 AND ?3 ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountNumberAndDateRange(String accountNumber, 
                                                       LocalDateTime startDate, 
                                                       LocalDateTime endDate);
    
    List<Transaction> findTop10BySenderAccountNumberOrReceiverAccountNumberOrderByCreatedAtDesc(
            String senderAccountNumber, String receiverAccountNumber);
}

