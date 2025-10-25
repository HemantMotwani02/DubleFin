package com.dublefin.user.service;

import com.dublefin.user.dto.BeneficiaryDto;
import com.dublefin.user.entity.Beneficiary;
import com.dublefin.user.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    @Transactional(readOnly = true)
    public List<BeneficiaryDto> getAllBeneficiaries(Long userId) {
        return beneficiaryRepository.findByUserIdAndIsActive(userId, true)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BeneficiaryDto addBeneficiary(Long userId, BeneficiaryDto beneficiaryDto) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setUserId(userId);
        beneficiary.setName(beneficiaryDto.getName());
        beneficiary.setAccountNumber(beneficiaryDto.getAccountNumber());
        beneficiary.setBankName(beneficiaryDto.getBankName());
        beneficiary.setIfscCode(beneficiaryDto.getIfscCode());
        beneficiary.setNickname(beneficiaryDto.getNickname());
        beneficiary.setType(beneficiaryDto.getType() != null ? 
                beneficiaryDto.getType() : Beneficiary.BeneficiaryType.INTERNAL);
        beneficiary.setIsActive(true);

        beneficiary = beneficiaryRepository.save(beneficiary);
        return convertToDto(beneficiary);
    }

    @Transactional
    public void deleteBeneficiary(Long userId, Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

        if (!beneficiary.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this beneficiary");
        }

        beneficiary.setIsActive(false);
        beneficiaryRepository.save(beneficiary);
    }

    private BeneficiaryDto convertToDto(Beneficiary beneficiary) {
        return BeneficiaryDto.builder()
                .id(beneficiary.getId())
                .name(beneficiary.getName())
                .accountNumber(beneficiary.getAccountNumber())
                .bankName(beneficiary.getBankName())
                .ifscCode(beneficiary.getIfscCode())
                .nickname(beneficiary.getNickname())
                .type(beneficiary.getType())
                .isActive(beneficiary.getIsActive())
                .build();
    }
}

