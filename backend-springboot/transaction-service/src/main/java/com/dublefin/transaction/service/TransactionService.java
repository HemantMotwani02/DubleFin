package com.dublefin.transaction.service;

import com.dublefin.transaction.dto.TransferRequest;
import com.dublefin.transaction.entity.Transaction;
import com.dublefin.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction processTransfer(TransferRequest request) {
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(request.getSenderAccountNumber());
        transaction.setReceiverAccountNumber(request.getReceiverAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setRemarks(request.getRemarks());
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionHistory(String accountNumber, int page, int size) {
        return transactionRepository.findByAccountNumber(
                accountNumber, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<Transaction> getRecentTransactions(String accountNumber) {
        return transactionRepository.findTop10BySenderAccountNumberOrReceiverAccountNumberOrderByCreatedAtDesc(
                accountNumber, accountNumber);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByAccountNumberAndDateRange(
                accountNumber, startDate, endDate);
    }
}

