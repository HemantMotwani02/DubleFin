package com.dublefin.user.dto;

import com.dublefin.user.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private UserProfile.Gender gender;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String profilePictureUrl;
    private UserProfile.KycStatus kycStatus;
    private String occupation;
    private String employer;
    private Double annualIncome;
}

