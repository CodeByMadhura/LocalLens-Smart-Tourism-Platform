package com.locallens.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequest {
    @NotBlank(message = "Target (email or phone) is required")
    private String target;
    
    @NotBlank(message = "Method is required")
    private String method;
}
