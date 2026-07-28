package com.locallens.config;

import com.locallens.entity.User;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;
import com.locallens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "testdevbyte@gmail.com";
        if (userRepository.findByEmailIgnoreCase(adminEmail) == null) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail(adminEmail);
            admin.setPhoneNumber("+919999999999");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setRole(UserRole.ADMIN);
            admin.setEmailVerified(true);
            admin.setPhoneVerified(true);
            admin.setActive(true);
            admin.setProfileCompleted(true);
            admin.setVerificationStatus(VerificationStatus.VERIFIED);

            userRepository.save(admin);
            System.out.println("--------------------------------------------------");
            System.out.println("INITIALIZED ADMIN ACCOUNT: " + adminEmail);
            System.out.println("--------------------------------------------------");
        }
    }
}
