package com.dublefin.user.service;

import com.dublefin.user.dto.KycSubmissionDto;
import com.dublefin.user.dto.UserProfileDto;
import com.dublefin.user.entity.UserProfile;
import com.dublefin.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfileDto getUserProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        
        return convertToDto(profile);
    }

    @Transactional
    public UserProfileDto createProfile(Long userId, String name, String email) {
        if (userProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setName(name);
        profile.setEmail(email);
        profile.setKycStatus(UserProfile.KycStatus.PENDING);
        
        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    @Transactional
    public UserProfileDto updateProfile(Long userId, UserProfileDto profileDto) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        if (profileDto.getName() != null) profile.setName(profileDto.getName());
        if (profileDto.getPhone() != null) {
            if (userProfileRepository.existsByPhone(profileDto.getPhone()) && 
                !profileDto.getPhone().equals(profile.getPhone())) {
                throw new RuntimeException("Phone number already in use");
            }
            profile.setPhone(profileDto.getPhone());
        }
        if (profileDto.getDateOfBirth() != null) profile.setDateOfBirth(profileDto.getDateOfBirth());
        if (profileDto.getGender() != null) profile.setGender(profileDto.getGender());
        if (profileDto.getAddress() != null) profile.setAddress(profileDto.getAddress());
        if (profileDto.getCity() != null) profile.setCity(profileDto.getCity());
        if (profileDto.getState() != null) profile.setState(profileDto.getState());
        if (profileDto.getCountry() != null) profile.setCountry(profileDto.getCountry());
        if (profileDto.getPostalCode() != null) profile.setPostalCode(profileDto.getPostalCode());
        if (profileDto.getOccupation() != null) profile.setOccupation(profileDto.getOccupation());
        if (profileDto.getEmployer() != null) profile.setEmployer(profileDto.getEmployer());
        if (profileDto.getAnnualIncome() != null) profile.setAnnualIncome(profileDto.getAnnualIncome());

        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    @Transactional
    public UserProfileDto submitKyc(Long userId, KycSubmissionDto kycDto) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setKycDocumentType(kycDto.getDocumentType());
        profile.setKycDocumentNumber(kycDto.getDocumentNumber());
        profile.setKycDocumentUrl(kycDto.getDocumentUrl());
        profile.setKycStatus(UserProfile.KycStatus.SUBMITTED);

        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    @Transactional
    public UserProfileDto approveKyc(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setKycStatus(UserProfile.KycStatus.APPROVED);
        profile.setKycVerifiedAt(LocalDateTime.now());

        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    @Transactional
    public UserProfileDto rejectKyc(Long userId, String reason) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setKycStatus(UserProfile.KycStatus.REJECTED);

        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    private UserProfileDto convertToDto(UserProfile profile) {
        return UserProfileDto.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .name(profile.getName())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .postalCode(profile.getPostalCode())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .kycStatus(profile.getKycStatus())
                .occupation(profile.getOccupation())
                .employer(profile.getEmployer())
                .annualIncome(profile.getAnnualIncome())
                .build();
    }
}

