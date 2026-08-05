package com.locallens.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String gender;
    private String role;

    private boolean active;
    private boolean emailVerified;

    private String profileImageUrl;

    private LocalDateTime createdAt;
}