package com.locallens.controller;

import com.locallens.dto.auth.RegisterRequest;
import com.locallens.dto.auth.RegisterResponse;
import com.locallens.dto.auth.ResendEmailVerificationRequest;
import com.locallens.dto.auth.SendOtpRequest;
import com.locallens.dto.auth.VerifyPhoneOtpRequest;
import com.locallens.dto.auth.LoginOtpRequest;
import com.locallens.dto.auth.LoginResponse;
import com.locallens.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
            authService.register(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(
            @RequestParam String token) {

        String message =
            authService.verifyEmail(token);

        return ResponseEntity.ok(
            Map.of("message", message)
        );
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<Map<String, String>>
    resendEmailVerification(
        @Valid
        @RequestBody
        ResendEmailVerificationRequest request) {

        String message =
            authService.resendEmailVerification(
                request.getEmail()
            );

        return ResponseEntity.ok(
            Map.of("message", message)
        );
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {
        
        String message = authService.sendOtp(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/verify-phone-otp")
    public ResponseEntity<Map<String, String>> verifyPhoneOtp(
            @Valid @RequestBody VerifyPhoneOtpRequest request) {
        
        String message = authService.verifyPhoneOtp(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/login-otp")
    public ResponseEntity<LoginResponse> loginOtp(
            @Valid @RequestBody LoginOtpRequest request) {
        
        LoginResponse response = authService.loginOtp(request);
        return ResponseEntity.ok(response);
    }
}