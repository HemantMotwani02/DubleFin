package com.dublefin.auth.repository;

import com.dublefin.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAccountNumber(String accountNumber);
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
}

