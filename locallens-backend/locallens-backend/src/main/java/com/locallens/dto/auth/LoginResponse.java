package com.locallens.dto.auth;

import com.locallens.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private boolean profileCompleted;
}
