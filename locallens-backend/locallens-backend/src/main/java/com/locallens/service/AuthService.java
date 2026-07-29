package com.locallens.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.auth.RegisterRequest;
import com.locallens.dto.auth.RegisterResponse;
import com.locallens.entity.EmailVerificationToken;
import com.locallens.entity.User;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;
import com.locallens.repository.EmailVerificationTokenRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.locallens.dto.auth.SendOtpRequest;
import com.locallens.dto.auth.VerifyPhoneOtpRequest;
import com.locallens.dto.auth.LoginOtpRequest;
import com.locallens.dto.auth.LoginResponse;
import com.locallens.repository.PhoneVerificationOtpRepository;
import com.locallens.entity.PhoneVerificationOtp;
import com.locallens.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PhoneVerificationOtpRepository phoneOtpRepository;
    private final JwtService jwtService;

    private final EmailVerificationTokenRepository
            tokenRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new traveller or local guide.
     */
    @Transactional
    public RegisterResponse register(
            RegisterRequest request
    ) {
        String normalizedEmail =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        String normalizedPhone =
                request.getPhoneNumber().trim();

        if (userRepository.existsByEmailIgnoreCase(
                normalizedEmail
        )) {
            throw new IllegalArgumentException(
                "Email address is already registered"
            );
        }

        if (userRepository.existsByPhoneNumber(
                normalizedPhone
        )) {
            throw new IllegalArgumentException(
                "Phone number is already registered"
            );
        }

        if (request.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException(
                "Admin self-registration is not allowed"
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(normalizedEmail);
        user.setPhoneNumber(normalizedPhone);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setActive(false);
        user.setProfileCompleted(false);
        user.setVerificationStatus(VerificationStatus.PENDING);

        User savedUser = userRepository.save(user);

        createAndSendEmailVerificationToken(
                savedUser
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Registration successful. " +
                "Verification link sent to email."
        );
    }

    /**
     * Creates and sends a new email verification token.
     */
    private void createAndSendEmailVerificationToken(
            User user
    ) {
        // Clean up old tokens using a traditional for loop
        java.util.List<EmailVerificationToken> oldTokens = tokenRepository.findByUserAndUsedFalse(user);
        for (EmailVerificationToken oldToken : oldTokens) {
            oldToken.setUsed(true);
        }

        String tokenValue = UUID.randomUUID().toString();

        // Create new token without builder
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setToken(tokenValue);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        token.setUsed(false);

        tokenRepository.save(token);

        emailService.sendVerificationEmail(
                user.getEmail(),
                tokenValue
        );
    }

    /**
     * Verifies the user's email address.
     */
    @Transactional
    public String verifyEmail(
            String tokenValue
    ) {
        if (
            tokenValue == null ||
            tokenValue.isBlank()
        ) {
            throw new IllegalArgumentException(
                "Verification token is required"
            );
        }

        EmailVerificationToken token = tokenRepository.findByTokenAndUsedFalse(tokenValue);
        
        if (token == null) {
            throw new IllegalArgumentException("Invalid or already used verification link");
        }

        if (
            token.getExpiresAt()
                .isBefore(LocalDateTime.now())
        ) {
            token.setUsed(true);
            tokenRepository.save(token);

            throw new IllegalArgumentException(
                "Verification link has expired"
            );
        }

        User user = token.getUser();

        if (user.isEmailVerified()) {
            token.setUsed(true);
            tokenRepository.save(token);

            return "Email is already verified";
        }

        token.setUsed(true);
        user.setEmailVerified(true);

        user.updateAccountStatus();

        tokenRepository.save(token);
        userRepository.save(user);

        if (!user.isPhoneVerified()) {
            return "Email verified successfully. " +
                   "Please verify your phone number.";
        }

        return "Email verified successfully. " +
               "You can now log in.";
    }

    /**
     * Sends a new email-verification link.
     */
    @Transactional
    public String resendEmailVerification(
            String email
    ) {
        if (
            email == null ||
            email.isBlank()
        ) {
            throw new IllegalArgumentException(
                "Email address is required"
            );
        }

        User user = userRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
        
        if (user == null) {
            throw new IllegalArgumentException("No account found with this email");
        }

        if (user.isEmailVerified()) {
            return "Email is already verified";
        }

        createAndSendEmailVerificationToken(user);

        return "A new verification link has been sent.";
    }

    @Transactional
    public String sendOtp(SendOtpRequest request) {
        String target = request.getTarget().trim();
        String method = request.getMethod();

        User user;
        if ("email".equalsIgnoreCase(method)) {
            user = userRepository.findByEmailIgnoreCase(target);
            if (user == null) {
                throw new IllegalArgumentException("No account found with this email");
            }
        } else if ("phone".equalsIgnoreCase(method)) {
            user = userRepository.findByPhoneNumber(target);
            if (user == null) {
                throw new IllegalArgumentException("No account found with this phone number");
            }
        } else {
            throw new IllegalArgumentException("Invalid method. Use 'email' or 'phone'");
        }

        // Clean up old OTPs for this user
        java.util.List<PhoneVerificationOtp> oldOtps = phoneOtpRepository.findByUserAndUsedFalse(user);
        for (PhoneVerificationOtp oldOtp : oldOtps) {
            oldOtp.setUsed(true);
            phoneOtpRepository.save(oldOtp);
        }

        // Generate 6-digit numeric OTP
        String rawOtp = String.format("%06d", new java.util.Random().nextInt(999999));

        PhoneVerificationOtp otpEntity = new PhoneVerificationOtp();
        otpEntity.setUser(user);
        otpEntity.setOtpHash(passwordEncoder.encode(rawOtp));
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpEntity.setUsed(false);
        otpEntity.setAttemptCount(0);

        phoneOtpRepository.save(otpEntity);

        if ("email".equalsIgnoreCase(method)) {
            emailService.sendOtpEmail(user.getEmail(), rawOtp);
        } else {
            // Print to standard out to simulate sending SMS
            System.out.println("----------------------------------------");
            System.out.println("LOCAL LENS SMS OTP SIMULATION");
            System.out.println("Recipient: " + target);
            System.out.println("Method: " + method);
            System.out.println("OTP Code: " + rawOtp);
            System.out.println("----------------------------------------");
        }

        return "OTP code sent successfully";
    }

    @Transactional
    public String verifyPhoneOtp(VerifyPhoneOtpRequest request) {
        String target = request.getTarget().trim();
        User user = userRepository.findByPhoneNumber(target);
        if (user == null) {
            user = userRepository.findByEmailIgnoreCase(target);
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        PhoneVerificationOtp otpEntity = phoneOtpRepository.findTopByUserAndUsedFalseOrderByCreatedAtDesc(user);
        if (otpEntity == null) {
            throw new IllegalArgumentException("No active OTP request found");
        }

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpEntity.setUsed(true);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("OTP code has expired. Please request a new one.");
        }

        if (otpEntity.getAttemptCount() >= 3) {
            otpEntity.setUsed(true);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("Maximum attempts reached. Please request a new code.");
        }

        if (!passwordEncoder.matches(request.getOtp(), otpEntity.getOtpHash())) {
            otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("Invalid OTP code.");
        }

        otpEntity.setUsed(true);
        user.setPhoneVerified(true);
        user.updateAccountStatus();

        phoneOtpRepository.save(otpEntity);
        userRepository.save(user);

        return "Phone number verified successfully";
    }

    @Transactional
    public LoginResponse loginOtp(LoginOtpRequest request) {
        String target = request.getTarget().trim();
        String method = request.getMethod();

        User user;
        if ("email".equalsIgnoreCase(method)) {
            user = userRepository.findByEmailIgnoreCase(target);
            if (user == null) {
                throw new IllegalArgumentException("No account found with this email");
            }
        } else if ("phone".equalsIgnoreCase(method)) {
            user = userRepository.findByPhoneNumber(target);
            if (user == null) {
                throw new IllegalArgumentException("No account found with this phone number");
            }
        } else {
            throw new IllegalArgumentException("Invalid method.");
        }

        PhoneVerificationOtp otpEntity = phoneOtpRepository.findTopByUserAndUsedFalseOrderByCreatedAtDesc(user);
        if (otpEntity == null) {
            throw new IllegalArgumentException("No active OTP request found");
        }

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpEntity.setUsed(true);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("OTP code has expired.");
        }

        if (otpEntity.getAttemptCount() >= 3) {
            otpEntity.setUsed(true);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("Maximum attempts reached.");
        }

        String submittedOtp = request.getOtp() != null ? request.getOtp().trim() : "";
        if (!passwordEncoder.matches(submittedOtp, otpEntity.getOtpHash())) {
            otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
            phoneOtpRepository.save(otpEntity);
            throw new IllegalArgumentException("Invalid OTP code.");
        }

        // OTP is valid
        otpEntity.setUsed(true);
        phoneOtpRepository.save(otpEntity);

        // Mark as verified based on method
        if ("email".equalsIgnoreCase(method)) {
            user.setEmailVerified(true);
        } else if ("phone".equalsIgnoreCase(method)) {
            user.setPhoneVerified(true);
        }
        
        // Update active status
        if (user.isEmailVerified() && user.isPhoneVerified()) {
            user.setActive(true);
        }
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        
        return new LoginResponse(
            jwtToken,
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole(),
            user.isProfileCompleted()
        );
    }
}