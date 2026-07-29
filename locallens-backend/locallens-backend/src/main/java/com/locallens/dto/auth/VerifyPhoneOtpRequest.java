package com.locallens.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyPhoneOtpRequest {
    @NotBlank(message = "Target (email or phone) is required")
    private String target;
    
    @NotBlank(message = "OTP is required")
    private String otp;
}
