package com.locallens.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.locallens.dto.auth.ForgotPasswordRequest;
import com.locallens.dto.auth.LoginOtpRequest;
import com.locallens.dto.auth.LoginRequest;
import com.locallens.dto.auth.LoginResponse;
import com.locallens.dto.auth.RegisterRequest;
import com.locallens.dto.auth.RegisterResponse;
import com.locallens.dto.auth.ResendEmailVerificationRequest;
import com.locallens.dto.auth.ResetPasswordRequest;
import com.locallens.dto.auth.SendOtpRequest;
import com.locallens.dto.auth.VerifyForgotPasswordOtpRequest;
import com.locallens.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a traveller or local guide.
     *
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Verifies registration email OTP.
     *
     * POST /api/auth/verify-email-otp
     */
    @PostMapping("/verify-email-otp")
    public ResponseEntity<Map<String, String>> verifyEmailOtp(
            @Valid @RequestBody LoginOtpRequest request
    ) {

        String message =
                authService.verifyEmailOtp(request);

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    /**
     * Resends registration verification OTP.
     *
     * POST /api/auth/resend-email-otp
     */
    @PostMapping("/resend-email-otp")
    public ResponseEntity<Map<String, String>> resendEmailOtp(
            @Valid
            @RequestBody
            ResendEmailVerificationRequest request
    ) {

        String message =
                authService.resendEmailOtp(
                        request.getEmail()
                );

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    /**
     * Logs in using email and password.
     *
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Sends OTP for email OTP login.
     *
     * POST /api/auth/send-login-otp
     */
    @PostMapping("/send-login-otp")
    public ResponseEntity<Map<String, String>> sendLoginOtp(
            @Valid @RequestBody SendOtpRequest request
    ) {

        String message =
                authService.sendLoginOtp(
                        request.getEmail()
                );

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    /**
     * Verifies login OTP and returns JWT.
     *
     * POST /api/auth/login-with-otp
     */
    @PostMapping("/login-with-otp")
    public ResponseEntity<LoginResponse> loginWithOtp(
            @Valid @RequestBody LoginOtpRequest request
    ) {

        LoginResponse response =
                authService.verifyLoginOtp(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Sends password-reset OTP.
     *
     * POST /api/auth/forgot-password
     *
     * Request:
     * {
     *   "email": "user@example.com"
     * }
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        String message =
                authService.sendForgotPasswordOtp(
                        request.getEmail()
                );

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    /**
     * Verifies password-reset OTP.
     *
     * POST /api/auth/verify-forgot-password-otp
     *
     * Request:
     * {
     *   "email": "user@example.com",
     *   "otp": "123456"
     * }
     */
    @PostMapping("/verify-forgot-password-otp")
    public ResponseEntity<Map<String, String>> verifyForgotPasswordOtp(
            @Valid
            @RequestBody
            VerifyForgotPasswordOtpRequest request
    ) {

        String message =
                authService.verifyForgotPasswordOtp(
                        request
                );

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    /**
     * Resets the user's password.
     *
     * POST /api/auth/reset-password
     *
     * Request:
     * {
     *   "email": "user@example.com",
     *   "otp": "123456",
     *   "newPassword": "NewPassword@123",
     *   "confirmPassword": "NewPassword@123"
     * }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request
    ) {

        String message =
                authService.resetPassword(request);

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }
}