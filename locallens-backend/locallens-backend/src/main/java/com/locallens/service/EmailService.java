package com.locallens.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String senderEmail;
	
	@Value("${app.frontend-url}")
	private String frontendUrl;
	
	public void sendVerificationEmail(String recipientEmail, String token) {
		String verificationLink = frontendUrl + "/verify-email?token=" + token;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail);
		message.setTo(recipientEmail);
		message.setSubject("Verify your LocalLens Account");
		message.setText("Please click this link to verify your LocalLens Account: " + verificationLink);
		
		try {
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("WARNING: Failed to send verification email to " + recipientEmail + ": " + e.getMessage());
		}
	}
	
	public void sendOtpEmail(String recipientEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail);
		message.setTo(recipientEmail);
		message.setSubject("Your LocalLens Verification Code");
		message.setText("Your 6-digit verification code is: " + otp + "\n\nThis code will expire in 5 minutes.");
		
		try {
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("WARNING: Failed to send OTP email to " + recipientEmail + ": " + e.getMessage());
		}
		
		// Always log the OTP to standard out so that developers can read it in dev environments
		System.out.println("----------------------------------------");
		System.out.println("LOCAL LENS EMAIL OTP SIMULATION");
		System.out.println("Recipient: " + recipientEmail);
		System.out.println("OTP Verification Code: " + otp);
		System.out.println("----------------------------------------");
	}
}
