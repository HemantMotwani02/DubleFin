package com.dublefin.account.service;

import com.dublefin.account.entity.Account;
import com.dublefin.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public Account createAccount(Long userId, String accountNumber, Account.AccountType accountType) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(0.0);
        account.setStatus(Account.AccountStatus.ACTIVE);
        
        if (accountType == Account.AccountType.SAVINGS) {
            account.setInterestRate(4.0);
        } else if (accountType == Account.AccountType.FIXED_DEPOSIT) {
            account.setInterestRate(6.5);
        }
        
        return accountRepository.save(account);
    }

    @Transactional
    public Account updateBalance(String accountNumber, Double amount, boolean isCredit) {
        Account account = getAccountByNumber(accountNumber);
        
        if (isCredit) {
            account.setBalance(account.getBalance() + amount);
        } else {
            if (account.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }
            account.setBalance(account.getBalance() - amount);
        }
        
        return accountRepository.save(account);
    }
}

