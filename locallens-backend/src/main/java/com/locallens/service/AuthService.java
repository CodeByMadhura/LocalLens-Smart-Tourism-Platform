package com.locallens.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.dto.auth.LoginOtpRequest;
import com.locallens.dto.auth.LoginRequest;
import com.locallens.dto.auth.LoginResponse;
import com.locallens.dto.auth.RegisterRequest;
import com.locallens.dto.auth.RegisterResponse;
import com.locallens.dto.auth.ResetPasswordRequest;
import com.locallens.dto.auth.VerifyForgotPasswordOtpRequest;
import com.locallens.entities.EmailVerificationToken;
import com.locallens.entities.LoginOtp;
import com.locallens.entities.User;
import com.locallens.enums.OtpChannel;
import com.locallens.enums.OtpPurpose;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;
import com.locallens.repository.EmailVerificationTokenRepository;
import com.locallens.repository.LoginOtpRepository;
import com.locallens.repository.UserRepository;
import com.locallens.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final long REGISTRATION_OTP_EXPIRATION_MINUTES = 10;
    private static final long LOGIN_OTP_EXPIRATION_MINUTES = 10;
    private static final long PASSWORD_RESET_OTP_EXPIRATION_MINUTES = 10;

    private static final int MAX_OTP_ATTEMPTS = 5;

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final UserRepository userRepository;

    private final EmailVerificationTokenRepository
            tokenRepository;

    private final LoginOtpRepository loginOtpRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    /*
     * ============================================================
     * REGISTRATION
     * ============================================================
     */

    /**
     * Registers a traveller or local guide and sends a six-digit
     * registration OTP to the user's email address.
     */
    @Transactional
    public RegisterResponse register(
            RegisterRequest request
    ) {

        validateRegisterRequest(request);

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        String normalizedPhone =
                normalizePhoneNumber(
                        request.getPhoneNumber()
                );

        if (
            userRepository.existsByEmailIgnoreCase(
                    normalizedEmail
            )
        ) {
            throw new IllegalArgumentException(
                    "Email address is already registered"
            );
        }

        if (
            normalizedPhone != null
            && userRepository.existsByPhoneNumber(
                    normalizedPhone
            )
        ) {
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

        user.setFirstName(
                request.getFirstName().trim()
        );

        user.setLastName(
                request.getLastName().trim()
        );

        user.setEmail(normalizedEmail);

        user.setPhoneNumber(normalizedPhone);

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());

        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setActive(false);
        user.setProfileCompleted(false);

        user.setVerificationStatus(
                VerificationStatus.PENDING
        );

        User savedUser =
                userRepository.save(user);

        createAndSendRegistrationOtp(
                savedUser
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Registration successful. "
                        + "A 6-digit verification OTP has been sent "
                        + "to your email address."
        );
    }

    /**
     * Verifies the registration email OTP.
     */
    @Transactional
    public String verifyEmailOtp(
            LoginOtpRequest request
    ) {

        validateOtpRequest(
                request,
                "Email verification request is required"
        );

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid email address or OTP"
                    )
                );

        if (user.isEmailVerified()) {
            return "Email address is already verified. "
                    + "You can log in.";
        }

        EmailVerificationToken token =
                tokenRepository
                    .findTopByUserAndTokenAndUsedFalseOrderByCreatedAtDesc(
                            user,
                            request.getOtp().trim()
                    )
                    .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Invalid or already-used verification OTP"
                        )
                    );

        if (
            token.getExpiresAt() == null
            || token.getExpiresAt()
                    .isBefore(LocalDateTime.now())
        ) {
            token.setUsed(true);

            tokenRepository.save(token);

            throw new IllegalArgumentException(
                    "Verification OTP has expired. "
                            + "Please request a new OTP."
            );
        }

        token.setUsed(true);

        tokenRepository.save(token);

        user.setEmailVerified(true);
        user.updateAccountStatus();

        user.setVerificationStatus(
                VerificationStatus.VERIFIED
        );

        userRepository.save(user);

        return "Email verified successfully. "
                + "You can now log in.";
    }

    /**
     * Resends the registration verification OTP.
     */
    @Transactional
    public String resendEmailOtp(
            String email
    ) {

        validateEmail(email);

        String normalizedEmail =
                normalizeEmail(email);

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "No account was found with this email address"
                    )
                );

        if (user.isEmailVerified()) {
            return "Email address is already verified. "
                    + "You can log in.";
        }

        createAndSendRegistrationOtp(user);

        return "A new verification OTP has been sent "
                + "to your email address.";
    }

    /*
     * ============================================================
     * PASSWORD LOGIN
     * ============================================================
     */

    /**
     * Logs in using email and password.
     */
    @Transactional(readOnly = true)
    public LoginResponse login(
            LoginRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Login request is required"
            );
        }

        validateEmail(request.getEmail());

        if (
            request.getPassword() == null
            || request.getPassword().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid email or password"
                    )
                );

        if (
            !passwordEncoder.matches(
                    request.getPassword(),
                    user.getPasswordHash()
            )
        ) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        validateUserForLogin(user);

        return createLoginResponse(user);
    }

    /*
     * ============================================================
     * EMAIL OTP LOGIN
     * ============================================================
     */

    /**
     * Sends an OTP for passwordless email login.
     */
    @Transactional
    public String sendLoginOtp(
            String email
    ) {

        validateEmail(email);

        String normalizedEmail =
                normalizeEmail(email);

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "No account was found with this email address"
                    )
                );

        validateUserForLogin(user);

        invalidateOldOtps(
                user,
                OtpPurpose.LOGIN
        );

        String otp =
                generateSixDigitOtp();

        LoginOtp loginOtp =
                new LoginOtp(
                        user,
                        otp,
                        OtpChannel.EMAIL,
                        OtpPurpose.LOGIN,
                        LocalDateTime.now()
                            .plusMinutes(
                                LOGIN_OTP_EXPIRATION_MINUTES
                            )
                );

        loginOtpRepository.save(loginOtp);

        emailService.sendLoginOtpEmail(
                user.getEmail(),
                otp
        );

        return "Login OTP has been sent "
                + "to your email address.";
    }

    /**
     * Verifies the login OTP and returns a JWT.
     */
    @Transactional
    public LoginResponse verifyLoginOtp(
            LoginOtpRequest request
    ) {

        validateOtpRequest(
                request,
                "Login OTP request is required"
        );

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid email address or OTP"
                    )
                );

        validateUserForLogin(user);

        LoginOtp loginOtp =
                getLatestActiveOtp(
                        user,
                        OtpPurpose.LOGIN,
                        "No active login OTP was found. "
                                + "Please request a new OTP."
                );

        validateOtpValue(
                loginOtp,
                request.getOtp(),
                "Login OTP has expired. "
                        + "Please request a new OTP."
        );

        loginOtp.markAsUsed();

        loginOtpRepository.save(loginOtp);

        return createLoginResponse(user);
    }

    /*
     * ============================================================
     * FORGOT PASSWORD
     * ============================================================
     */

    /**
     * Sends a six-digit OTP for resetting the password.
     *
     * POST /api/auth/forgot-password
     */
    @Transactional
    public String sendForgotPasswordOtp(
            String email
    ) {

        validateEmail(email);

        String normalizedEmail =
                normalizeEmail(email);

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "No account was found with this email address"
                    )
                );

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException(
                    "Please verify your email address before "
                            + "resetting your password."
            );
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "Your account is inactive."
            );
        }

        /*
         * Mark all old password-reset OTPs as used.
         */
        invalidateOldOtps(
                user,
                OtpPurpose.PASSWORD_RESET
        );

        String otp =
                generateSixDigitOtp();

        LoginOtp resetOtp =
                new LoginOtp(
                        user,
                        otp,
                        OtpChannel.EMAIL,
                        OtpPurpose.PASSWORD_RESET,
                        LocalDateTime.now()
                            .plusMinutes(
                                PASSWORD_RESET_OTP_EXPIRATION_MINUTES
                            )
                );

        loginOtpRepository.save(resetOtp);

        emailService.sendForgotPasswordOtpEmail(
                user.getEmail(),
                otp
        );

        return "A password reset OTP has been sent "
                + "to your email address.";
    }

    /**
     * Verifies the password-reset OTP.
     *
     * This method validates the OTP but does not mark it as used.
     * It is marked as used only after the password is successfully reset.
     */
    @Transactional
    public String verifyForgotPasswordOtp(
            VerifyForgotPasswordOtpRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Password reset OTP request is required"
            );
        }

        validateEmail(request.getEmail());
        validateOtp(request.getOtp());

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid email address or OTP"
                    )
                );

        LoginOtp resetOtp =
                getLatestActiveOtp(
                        user,
                        OtpPurpose.PASSWORD_RESET,
                        "No active password reset OTP was found. "
                                + "Please request a new OTP."
                );

        validateOtpValue(
                resetOtp,
                request.getOtp(),
                "Password reset OTP has expired. "
                        + "Please request a new OTP."
        );

        return "OTP verified successfully. "
                + "You can now create a new password.";
    }

    /**
     * Resets the user's password after validating the reset OTP.
     *
     * POST /api/auth/reset-password
     */
    @Transactional
    public String resetPassword(
            ResetPasswordRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Reset password request is required"
            );
        }

        validateEmail(request.getEmail());
        validateOtp(request.getOtp());

        if (
            request.getNewPassword() == null
            || request.getNewPassword().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "New password is required"
            );
        }

        if (
            request.getNewPassword().length() < 8
        ) {
            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters"
            );
        }

        if (
            request.getConfirmPassword() == null
            || request.getConfirmPassword().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Confirm password is required"
            );
        }

        if (
            !request.getNewPassword().equals(
                    request.getConfirmPassword()
            )
        ) {
            throw new IllegalArgumentException(
                    "New password and confirm password do not match"
            );
        }

        String normalizedEmail =
                normalizeEmail(request.getEmail());

        User user = userRepository
                .findByEmailIgnoreCase(
                        normalizedEmail
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid password reset request"
                    )
                );

        LoginOtp resetOtp =
                getLatestActiveOtp(
                        user,
                        OtpPurpose.PASSWORD_RESET,
                        "No active password reset OTP was found. "
                                + "Please request a new OTP."
                );

        validateOtpValue(
                resetOtp,
                request.getOtp(),
                "Password reset OTP has expired. "
                        + "Please request a new OTP."
        );

        /*
         * Optional restriction:
         * Do not allow setting the same password again.
         */
        if (
            passwordEncoder.matches(
                    request.getNewPassword(),
                    user.getPasswordHash()
            )
        ) {
            throw new IllegalArgumentException(
                    "New password must be different from "
                            + "your current password"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        /*
         * Prevent reuse of the same password-reset OTP.
         */
        resetOtp.markAsUsed();

        loginOtpRepository.save(resetOtp);

        return "Password reset successfully. "
                + "Please log in using your new password.";
    }

    /*
     * ============================================================
     * PRIVATE OTP METHODS
     * ============================================================
     */

    /**
     * Marks all old unused OTPs for the given purpose as used.
     */
    private void invalidateOldOtps(
            User user,
            OtpPurpose purpose
    ) {

        List<LoginOtp> oldOtps =
                loginOtpRepository
                    .findByUserAndPurposeAndUsedFalse(
                            user,
                            purpose
                    );

        for (LoginOtp oldOtp : oldOtps) {
            oldOtp.markAsUsed();
        }

        if (!oldOtps.isEmpty()) {
            loginOtpRepository.saveAll(oldOtps);
        }
    }

    /**
     * Gets the newest unused email OTP for a particular purpose.
     */
    private LoginOtp getLatestActiveOtp(
            User user,
            OtpPurpose purpose,
            String missingOtpMessage
    ) {

        return loginOtpRepository
                .findTopByUserAndChannelAndPurposeAndUsedFalseOrderByCreatedAtDesc(
                        user,
                        OtpChannel.EMAIL,
                        purpose
                )
                .orElseThrow(
                    () -> new IllegalArgumentException(
                            missingOtpMessage
                    )
                );
    }

    /**
     * Checks expiration, maximum attempts and OTP value.
     */
    private void validateOtpValue(
            LoginOtp loginOtp,
            String requestedOtp,
            String expiredMessage
    ) {

        if (loginOtp.isExpired()) {
            loginOtp.markAsUsed();

            loginOtpRepository.save(loginOtp);

            throw new IllegalArgumentException(
                    expiredMessage
            );
        }

        if (
            loginOtp.getAttemptCount()
                    >= MAX_OTP_ATTEMPTS
        ) {
            loginOtp.markAsUsed();

            loginOtpRepository.save(loginOtp);

            throw new IllegalArgumentException(
                    "Maximum OTP attempts exceeded. "
                            + "Please request a new OTP."
            );
        }

        if (
            !loginOtp.getOtp().equals(
                    requestedOtp.trim()
            )
        ) {
            loginOtp.incrementAttemptCount();

            if (
                loginOtp.getAttemptCount()
                        >= MAX_OTP_ATTEMPTS
            ) {
                loginOtp.markAsUsed();
            }

            loginOtpRepository.save(loginOtp);

            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }
    }

    /**
     * Invalidates old registration OTPs and sends a new one.
     */
    private void createAndSendRegistrationOtp(
            User user
    ) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User is required to create a verification OTP"
            );
        }

        List<EmailVerificationToken> oldTokens =
                tokenRepository
                    .findByUserAndUsedFalse(user);

        for (
            EmailVerificationToken oldToken
                : oldTokens
        ) {
            oldToken.setUsed(true);
        }

        if (!oldTokens.isEmpty()) {
            tokenRepository.saveAll(oldTokens);
        }

        String otp =
                generateSixDigitOtp();

        EmailVerificationToken token =
                new EmailVerificationToken();

        token.setUser(user);
        token.setToken(otp);

        token.setExpiresAt(
                LocalDateTime.now()
                    .plusMinutes(
                        REGISTRATION_OTP_EXPIRATION_MINUTES
                    )
        );

        token.setUsed(false);

        tokenRepository.save(token);

        emailService.sendRegistrationOtpEmail(
                user.getEmail(),
                otp
        );
    }

    /*
     * ============================================================
     * PRIVATE VALIDATION METHODS
     * ============================================================
     */

    private void validateUserForLogin(
            User user
    ) {

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException(
                    "Please verify your email using the registration OTP "
                            + "before logging in"
            );
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "Your account is inactive"
            );
        }
    }

    private LoginResponse createLoginResponse(
            User user
    ) {

        String jwtToken =
                jwtService.generateToken(user);

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

    private String generateSixDigitOtp() {

        int otpNumber =
                100000
                + SECURE_RANDOM.nextInt(900000);

        return String.valueOf(
                otpNumber
        );
    }

    private void validateOtpRequest(
            LoginOtpRequest request,
            String nullRequestMessage
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    nullRequestMessage
            );
        }

        validateEmail(request.getEmail());
        validateOtp(request.getOtp());
    }

    private void validateOtp(
            String otp
    ) {

        if (
            otp == null
            || otp.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "OTP is required"
            );
        }

        if (
            !otp.trim().matches("\\d{6}")
        ) {
            throw new IllegalArgumentException(
                    "OTP must be exactly 6 digits"
            );
        }
    }

    private void validateEmail(
            String email
    ) {

        if (
            email == null
            || email.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Email address is required"
            );
        }
    }

    private void validateRegisterRequest(
            RegisterRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Registration request is required"
            );
        }

        if (
            request.getFirstName() == null
            || request.getFirstName().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "First name is required"
            );
        }

        if (
            request.getLastName() == null
            || request.getLastName().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Last name is required"
            );
        }

        validateEmail(request.getEmail());

        if (
            request.getPassword() == null
            || request.getPassword().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        if (request.getRole() == null) {
            throw new IllegalArgumentException(
                    "User role is required"
            );
        }
    }

    private String normalizeEmail(
            String email
    ) {

        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private String normalizePhoneNumber(
            String phoneNumber
    ) {

        if (
            phoneNumber == null
            || phoneNumber.isBlank()
        ) {
            return null;
        }

        return phoneNumber.trim();
    }
}