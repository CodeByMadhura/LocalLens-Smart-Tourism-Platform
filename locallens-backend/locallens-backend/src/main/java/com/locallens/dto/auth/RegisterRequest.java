package com.locallens.dto.auth;

import com.locallens.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
	
	@NotBlank(message = "First Name is required")
	private String firstName;
	
	@NotBlank(message = "Last Name is required")
	private String lastName;
	
	@Email(message = "Enter a valid email address")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(
			 regexp = "^[6-9][0-9]{9}$",
		        message = "Enter a valid 10-digit phone number"
			)
    private String phoneNumber;
	
	@NotBlank(message = "Password is required")
	@Size(
			min = 8,
			message = "Password must contain at least 8 characters"
			)
	private String password;
	
	@NotNull(message = "User role is required")
	private UserRole role;
	
	
	
}
