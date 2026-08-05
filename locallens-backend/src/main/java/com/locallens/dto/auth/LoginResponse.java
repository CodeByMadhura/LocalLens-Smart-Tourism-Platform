package com.locallens.dto.auth;

import com.locallens.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /*
     * Frontend expects this exact property name:
     *
     * response.data.token
     */
    private String token;

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private boolean profileCompleted;
}