package com.locallens.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.locallens.dto.guide.ProfilePhotoResponse;
import com.locallens.service.ProfilePhotoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/guide/profile/photo")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final ProfilePhotoService
        profilePhotoService;

    @PostMapping(
        consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProfilePhotoResponse>
            uploadProfilePhoto(
                @RequestPart("file")
                MultipartFile file,

                Authentication authentication
            ) {

        ProfilePhotoResponse response =
            profilePhotoService
                .uploadProfilePhoto(
                    authentication.getName(),
                    file
                );

        return ResponseEntity.ok(
            response
        );
    }

    @GetMapping
    public ResponseEntity<ProfilePhotoResponse>
            getProfilePhoto(
                Authentication authentication
            ) {

        return ResponseEntity.ok(
            profilePhotoService
                .getProfilePhoto(
                    authentication.getName()
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void>
            deleteProfilePhoto(
                Authentication authentication
            ) {

        profilePhotoService
            .deleteProfilePhoto(
                authentication.getName()
            );

        return ResponseEntity.noContent()
            .build();
    }
}