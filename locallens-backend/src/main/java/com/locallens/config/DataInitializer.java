package com.locallens.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.locallens.entities.User;
import com.locallens.enums.UserRole;
import com.locallens.enums.VerificationStatus;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL =
            "admin@locallens.com";

    private static final String ADMIN_PASSWORD =
            "Admin@123";

    private static final String ADMIN_PHONE =
            "9999999999";

    private static final String ADMIN_FIRST_NAME =
            "LocalLens";

    private static final String ADMIN_LAST_NAME =
            "Admin";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        String normalizedEmail =
                ADMIN_EMAIL.trim().toLowerCase();

        User existingAdmin =
                userRepository
                        .findByEmailIgnoreCase(
                                normalizedEmail
                        )
                        .orElse(null);

        if (existingAdmin != null) {

            System.out.println(
                    "--------------------------------------------------"
            );

            System.out.println(
                    "LocalLens admin already exists: "
                            + normalizedEmail
            );

            System.out.println(
                    "Admin ID: "
                            + existingAdmin.getId()
            );

            System.out.println(
                    "--------------------------------------------------"
            );

            return;
        }

        User admin = new User();

        admin.setFirstName(
                ADMIN_FIRST_NAME
        );

        admin.setLastName(
                ADMIN_LAST_NAME
        );

        admin.setEmail(
                normalizedEmail
        );

        /*
         * Required because phone_number is NOT NULL
         * in your users table.
         */
        admin.setPhoneNumber(
                ADMIN_PHONE
        );

        admin.setPasswordHash(
                passwordEncoder.encode(
                        ADMIN_PASSWORD
                )
        );

        admin.setRole(
                UserRole.ADMIN
        );

        admin.setEmailVerified(true);
        admin.setPhoneVerified(true);
        admin.setActive(true);
        admin.setProfileCompleted(true);

        admin.setVerificationStatus(
                VerificationStatus.VERIFIED
        );

        User savedAdmin =
                userRepository.save(admin);

        System.out.println(
                "--------------------------------------------------"
        );

        System.out.println(
                "LocalLens admin created successfully."
        );

        System.out.println(
                "Admin ID: "
                        + savedAdmin.getId()
        );

        System.out.println(
                "Admin email: "
                        + savedAdmin.getEmail()
        );

        System.out.println(
                "Admin role: "
                        + savedAdmin.getRole()
        );

        System.out.println(
                "--------------------------------------------------"
        );
    }
}