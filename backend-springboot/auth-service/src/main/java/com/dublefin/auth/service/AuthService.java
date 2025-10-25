package com.dublefin.auth.service;

import com.dublefin.auth.dto.*;
import com.dublefin.auth.entity.AuditLog;
import com.dublefin.auth.entity.RefreshToken;
import com.dublefin.auth.entity.User;
import com.dublefin.auth.repository.AuditLogRepository;
import com.dublefin.auth.repository.RefreshTokenRepository;
import com.dublefin.auth.repository.UserRepository;
import com.dublefin.auth.util.AccountNumberGenerator;
import com.dublefin.auth.util.JwtUtil;
import com.dublefin.auth.util.TwoFactorAuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TwoFactorAuthUtil twoFactorAuthUtil;
    private final AccountNumberGenerator accountNumberGenerator;
    private final HttpServletRequest request;

    @Value("${security.max-login-attempts}")
    private Integer maxLoginAttempts;

    @Value("${security.lockout-duration-minutes}")
    private Integer lockoutDuration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logAudit(null, "REGISTRATION_FAILED", "Email already exists: " + registerRequest.getEmail(), 
                    AuditLog.ActionStatus.FAILURE);
            throw new RuntimeException("Email is already registered");
        }

        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generateAccountNumber();
        } while (userRepository.existsByAccountNumber(accountNumber));

        // Create new user
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccountNumber(accountNumber);
        user.setBalance(0.0);
        user.setEnabled(true);
        user.setStatus(User.UserStatus.ACTIVE);
        user.setLastPasswordChange(LocalDateTime.now());

        user = userRepository.save(user);

        // Generate tokens
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = createRefreshToken(user.getId());

        logAudit(user.getId(), "REGISTRATION_SUCCESS", "User registered successfully", 
                AuditLog.ActionStatus.SUCCESS);

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accountNumber(user.getAccountNumber())
                .balance(user.getBalance())
                .token(token)
                .refreshToken(refreshToken)
                .twoFactorEnabled(false)
                .requiresTwoFactor(false)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    logAudit(null, "LOGIN_FAILED", "User not found: " + loginRequest.getEmail(), 
                            AuditLog.ActionStatus.FAILURE);
                    return new RuntimeException("Invalid credentials");
                });

        // Check if account is locked
        if (user.getLocked() && user.getLockTime() != null) {
            LocalDateTime unlockTime = user.getLockTime().plusMinutes(lockoutDuration);
            if (LocalDateTime.now().isBefore(unlockTime)) {
                logAudit(user.getId(), "LOGIN_FAILED", "Account is locked", 
                        AuditLog.ActionStatus.FAILURE);
                throw new RuntimeException("Account is locked. Please try again later.");
            } else {
                // Unlock account
                user.setLocked(false);
                user.setFailedLoginAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
            }
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            handleFailedLogin(user);
            logAudit(user.getId(), "LOGIN_FAILED", "Invalid password", 
                    AuditLog.ActionStatus.FAILURE);
            throw new RuntimeException("Invalid credentials");
        }

        // Check if 2FA is enabled
        if (user.getTwoFactorEnabled()) {
            if (loginRequest.getTwoFactorCode() == null || loginRequest.getTwoFactorCode().isEmpty()) {
                return AuthResponse.builder()
                        .requiresTwoFactor(true)
                        .email(user.getEmail())
                        .build();
            }

            // Verify 2FA code
            if (!twoFactorAuthUtil.verifyCode(user.getTwoFactorSecret(), loginRequest.getTwoFactorCode())) {
                handleFailedLogin(user);
                logAudit(user.getId(), "LOGIN_FAILED", "Invalid 2FA code", 
                        AuditLog.ActionStatus.FAILURE);
                throw new RuntimeException("Invalid 2FA code");
            }
        }

        // Reset failed attempts on successful login
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        // Generate tokens
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = createRefreshToken(user.getId());

        logAudit(user.getId(), "LOGIN_SUCCESS", "User logged in successfully", 
                AuditLog.ActionStatus.SUCCESS);

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accountNumber(user.getAccountNumber())
                .balance(user.getBalance())
                .token(token)
                .refreshToken(refreshToken)
                .twoFactorEnabled(user.getTwoFactorEnabled())
                .requiresTwoFactor(false)
                .build();
    }

    @Transactional
    public TwoFactorResponse setupTwoFactor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String secret = twoFactorAuthUtil.generateSecretKey();
        String qrCodeUrl = twoFactorAuthUtil.generateQRCodeUrl(user.getEmail(), secret);

        try {
            byte[] qrCodeImage = twoFactorAuthUtil.generateQRCodeImage(qrCodeUrl, 250, 250);
            String base64Image = Base64.getEncoder().encodeToString(qrCodeImage);

            user.setTwoFactorSecret(secret);
            userRepository.save(user);

            logAudit(userId, "2FA_SETUP_INITIATED", "2FA setup initiated", 
                    AuditLog.ActionStatus.SUCCESS);

            return new TwoFactorResponse(secret, base64Image, secret);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage());
        }
    }

    @Transactional
    public void enableTwoFactor(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTwoFactorSecret() == null) {
            throw new RuntimeException("2FA setup not initiated");
        }

        if (!twoFactorAuthUtil.verifyCode(user.getTwoFactorSecret(), code)) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        logAudit(userId, "2FA_ENABLED", "2FA enabled successfully", 
                AuditLog.ActionStatus.SUCCESS);
    }

    @Transactional
    public void disableTwoFactor(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getTwoFactorEnabled()) {
            throw new RuntimeException("2FA is not enabled");
        }

        if (!twoFactorAuthUtil.verifyCode(user.getTwoFactorSecret(), code)) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        userRepository.save(user);

        logAudit(userId, "2FA_DISABLED", "2FA disabled successfully", 
                AuditLog.ActionStatus.SUCCESS);
    }

    private String createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(userId);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= maxLoginAttempts) {
            user.setLocked(true);
            user.setLockTime(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    private void logAudit(Long userId, String action, String details, AuditLog.ActionStatus status) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        log.setIpAddress(getClientIP());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setStatus(status);
        auditLogRepository.save(log);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

