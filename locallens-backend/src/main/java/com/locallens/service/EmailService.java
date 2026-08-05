package com.locallens.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

    /**
     * Sends the six-digit OTP used to verify
     * a newly registered account.
     */
    public void sendRegistrationOtpEmail(
            String recipientEmail,
            String otp
    ) {

        validateRecipient(recipientEmail);

        validateOtp(
                otp,
                "Registration OTP is required"
        );

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipientEmail);

        message.setSubject(
                "Verify your LocalLens account"
        );

        message.setText(
                "Hello,\n\n"
                + "Thank you for registering with LocalLens.\n\n"
                + "Your email verification OTP is:\n\n"
                + otp
                + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "Do not share this OTP with anyone.\n\n"
                + "If you did not create this account, "
                + "you can ignore this email.\n\n"
                + "Regards,\n"
                + "LocalLens Team"
        );

        sendMessage(
                message,
                "Unable to send the registration OTP. "
                        + "Please try again later."
        );
    }

    /**
     * Sends the six-digit OTP used
     * for passwordless login.
     */
    public void sendLoginOtpEmail(
            String recipientEmail,
            String otp
    ) {

        validateRecipient(recipientEmail);

        validateOtp(
                otp,
                "Login OTP is required"
        );

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipientEmail);

        message.setSubject(
                "Your LocalLens login OTP"
        );

        message.setText(
                "Hello,\n\n"
                + "Your LocalLens login OTP is:\n\n"
                + otp
                + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "Do not share this OTP with anyone.\n\n"
                + "If you did not request this OTP, "
                + "you can ignore this email.\n\n"
                + "Regards,\n"
                + "LocalLens Team"
        );

        sendMessage(
                message,
                "Unable to send the login OTP. "
                        + "Please try again later."
        );
    }

    /**
     * Sends the six-digit OTP used
     * for resetting the user's password.
     */
    public void sendForgotPasswordOtpEmail(
            String recipientEmail,
            String otp
    ) {

        validateRecipient(recipientEmail);

        validateOtp(
                otp,
                "Password reset OTP is required"
        );

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipientEmail);

        message.setSubject(
                "Reset your LocalLens password"
        );

        message.setText(
                "Hello,\n\n"
                + "We received a request to reset "
                + "your LocalLens account password.\n\n"
                + "Your password reset OTP is:\n\n"
                + otp
                + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "Do not share this OTP with anyone.\n\n"
                + "If you did not request a password reset, "
                + "you can safely ignore this email. "
                + "Your current password will remain unchanged.\n\n"
                + "Regards,\n"
                + "LocalLens Team"
        );

        sendMessage(
                message,
                "Unable to send the password reset OTP. "
                        + "Please try again later."
        );
    }

    /**
     * Validates the email recipient.
     */
    private void validateRecipient(
            String recipientEmail
    ) {

        if (
            recipientEmail == null
            || recipientEmail.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Recipient email address is required"
            );
        }
    }

    /**
     * Validates that the OTP contains exactly six digits.
     */
    private void validateOtp(
            String otp,
            String missingOtpMessage
    ) {

        if (
            otp == null
            || otp.isBlank()
        ) {
            throw new IllegalArgumentException(
                    missingOtpMessage
            );
        }

        if (!otp.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "OTP must be exactly 6 digits"
            );
        }
    }

    /**
     * Sends an email and converts mail errors
     * into a readable backend exception.
     */
    private void sendMessage(
            SimpleMailMessage message,
            String errorMessage
    ) {

        try {
            mailSender.send(message);
        } catch (MailException exception) {
            throw new IllegalStateException(
                    errorMessage,
                    exception
            );
        }
    }
}