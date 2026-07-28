package com.locallens.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginOtpRequest {
    @NotBlank(message = "Target (email or phone) is required")
    private String target;
    
    @NotBlank(message = "OTP is required")
    private String otp;
    
    @NotBlank(message = "Method is required")
    private String method;
}
