package com.dublefin.user.repository;

import com.dublefin.user.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUserIdAndIsActive(Long userId, Boolean isActive);
    List<Beneficiary> findByUserId(Long userId);
}

