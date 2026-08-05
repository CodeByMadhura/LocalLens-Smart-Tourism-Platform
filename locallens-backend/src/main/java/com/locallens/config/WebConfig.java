package com.locallens.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
        implements WebMvcConfigurer {

    private static final String UPLOAD_URL_PATTERN =
            "/uploads/**";

    private static final String UPLOAD_DIRECTORY =
            "uploads";

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        /*
         * This resolves the uploads directory relative
         * to the backend project's running directory.
         *
         * Example Windows path:
         * D:/LocalLens Project/locallens-backend/uploads/
         */
        Path uploadDirectory =
                Paths.get(UPLOAD_DIRECTORY)
                        .toAbsolutePath()
                        .normalize();

        /*
         * Path.toUri().toString() automatically produces
         * the correct file URI:
         *
         * file:///D:/LocalLens%20Project/...
         */
        String uploadResourceLocation =
                uploadDirectory
                        .toUri()
                        .toString();

        registry
                .addResourceHandler(
                        UPLOAD_URL_PATTERN
                )
                .addResourceLocations(
                        uploadResourceLocation
                );
    }
}