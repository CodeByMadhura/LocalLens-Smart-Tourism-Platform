package com.locallens.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    private final Path placeUploadDirectory;

    public FileStorageService(
            @Value("${app.upload.place-directory:uploads/places}")
            String placeDirectory
    ) {
        this.placeUploadDirectory = Paths.get(placeDirectory)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.placeUploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not create place upload directory",
                    exception
            );
        }
    }

    public String storePlaceImage(MultipartFile file) {

        validateImage(file);

        String originalFileName = StringUtils.cleanPath(
                file.getOriginalFilename() == null
                        ? "place-image"
                        : file.getOriginalFilename()
        );

        String extension = getFileExtension(originalFileName);

        String generatedFileName =
                UUID.randomUUID() + extension;

        Path targetLocation =
                placeUploadDirectory.resolve(generatedFileName);

        try {
            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to save place image",
                    exception
            );
        }

        return "/uploads/places/" + generatedFileName;
    }

    public void deletePlaceImage(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String fileName = imageUrl.substring(
                imageUrl.lastIndexOf("/") + 1
        );

        try {
            Files.deleteIfExists(
                    placeUploadDirectory.resolve(fileName)
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to delete place image",
                    exception
            );
        }
    }

    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Image file cannot be empty"
            );
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed"
            );
        }

        long maximumSize = 5L * 1024L * 1024L;

        if (file.getSize() > maximumSize) {
            throw new IllegalArgumentException(
                    "Image size cannot exceed 5 MB"
            );
        }
    }

    private String getFileExtension(String fileName) {

        int lastDot = fileName.lastIndexOf(".");

        if (lastDot < 0) {
            return ".jpg";
        }

        return fileName.substring(lastDot).toLowerCase();
    }
}