package com.locallens.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.locallens.dto.guide.ProfilePhotoResponse;
import com.locallens.entities.User;
import com.locallens.entities.UserPhoto;
import com.locallens.repository.UserPhotoRepository;
import com.locallens.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private static final long MAX_FILE_SIZE =
        5 * 1024 * 1024;

    private static final Set<String>
        ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
        );

    private final UserRepository userRepository;

    private final UserPhotoRepository
        userPhotoRepository;

    private final Path uploadDirectory =
        Paths.get(
            "uploads",
            "profile-photos"
        ).toAbsolutePath().normalize();

    @Transactional
    public ProfilePhotoResponse uploadProfilePhoto(
            String authenticatedEmail,
            MultipartFile file
    ) {
        User user = findUser(
            authenticatedEmail
        );

        validateImage(file);

        try {
            Files.createDirectories(
                uploadDirectory
            );

            String extension =
                getExtension(
                    file.getOriginalFilename()
                );

            String storedFileName =
                "user-" +
                user.getId() +
                "-" +
                UUID.randomUUID() +
                extension;

            Path targetLocation =
                uploadDirectory.resolve(
                    storedFileName
                );

            Files.copy(
                file.getInputStream(),
                targetLocation,
                StandardCopyOption
                    .REPLACE_EXISTING
            );

            UserPhoto profilePhoto =
                userPhotoRepository
                    .findByUserIdAndProfilePhotoTrue(
                        user.getId()
                    )
                    .orElseGet(() ->
                        UserPhoto.builder()
                            .user(user)
                            .profilePhoto(true)
                            .description(
                                "Profile photo"
                            )
                            .build()
                    );

            /*
             * Delete the previous local file only after
             * the new image was successfully stored.
             */
            deletePreviousLocalFile(
                profilePhoto.getPhotoUrl()
            );

            String photoUrl =
                "/uploads/profile-photos/" +
                storedFileName;

            profilePhoto.setPhotoUrl(
                photoUrl
            );

            profilePhoto.setProfilePhoto(
                true
            );

            UserPhoto savedPhoto =
                userPhotoRepository.save(
                    profilePhoto
                );

            return ProfilePhotoResponse
                .builder()
                .photoId(
                    savedPhoto.getId()
                )
                .profileImageUrl(
                    photoUrl
                )
                .message(
                    "Profile photo uploaded successfully."
                )
                .build();

        } catch (IOException exception) {
            throw new RuntimeException(
                "Unable to store profile photo.",
                exception
            );
        }
    }

    @Transactional(readOnly = true)
    public ProfilePhotoResponse getProfilePhoto(
            String authenticatedEmail
    ) {
        User user = findUser(
            authenticatedEmail
        );

        return userPhotoRepository
            .findByUserIdAndProfilePhotoTrue(
                user.getId()
            )
            .map(photo ->
                ProfilePhotoResponse
                    .builder()
                    .photoId(
                        photo.getId()
                    )
                    .profileImageUrl(
                        photo.getPhotoUrl()
                    )
                    .message(
                        "Profile photo found."
                    )
                    .build()
            )
            .orElse(
                ProfilePhotoResponse
                    .builder()
                    .profileImageUrl(null)
                    .message(
                        "Profile photo not found."
                    )
                    .build()
            );
    }

    @Transactional
    public void deleteProfilePhoto(
            String authenticatedEmail
    ) {
        User user = findUser(
            authenticatedEmail
        );

        userPhotoRepository
            .findByUserIdAndProfilePhotoTrue(
                user.getId()
            )
            .ifPresent(photo -> {
                deletePreviousLocalFile(
                    photo.getPhotoUrl()
                );

                userPhotoRepository.delete(
                    photo
                );
            });
    }

    private User findUser(
            String email
    ) {
        return userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() ->
                new RuntimeException(
                    "Authenticated user was not found."
                )
            );
    }

    private void validateImage(
            MultipartFile file
    ) {
        if (
            file == null ||
            file.isEmpty()
        ) {
            throw new IllegalArgumentException(
                "Please select an image."
            );
        }

        if (
            file.getSize() >
            MAX_FILE_SIZE
        ) {
            throw new IllegalArgumentException(
                "Profile photo must be smaller than 5 MB."
            );
        }

        String contentType =
            file.getContentType();

        if (
            contentType == null ||
            !ALLOWED_CONTENT_TYPES.contains(
                contentType.toLowerCase()
            )
        ) {
            throw new IllegalArgumentException(
                "Only JPG, JPEG, PNG and WEBP images are allowed."
            );
        }
    }

    private String getExtension(
            String originalFileName
    ) {
        if (
            originalFileName == null ||
            !originalFileName.contains(".")
        ) {
            return ".jpg";
        }

        String extension =
            originalFileName.substring(
                originalFileName
                    .lastIndexOf(".")
            );

        return extension.toLowerCase();
    }

    private void deletePreviousLocalFile(
            String previousPhotoUrl
    ) {
        if (
            previousPhotoUrl == null ||
            previousPhotoUrl.isBlank() ||
            !previousPhotoUrl.startsWith(
                "/uploads/profile-photos/"
            )
        ) {
            return;
        }

        String fileName =
            previousPhotoUrl.substring(
                previousPhotoUrl
                    .lastIndexOf("/") + 1
            );

        try {
            Files.deleteIfExists(
                uploadDirectory.resolve(
                    fileName
                )
            );
        } catch (IOException exception) {
            /*
             * Do not fail the upload only because an old
             * image could not be deleted.
             */
            System.err.println(
                "Unable to delete previous profile photo: " +
                exception.getMessage()
            );
        }
    }
}