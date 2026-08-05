package com.locallens.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.locallens.dto.admin.AdminDashboardResponse;
import com.locallens.dto.admin.AdminUserResponse;
import com.locallens.dto.admin.UpdateUserStatusRequest;
import com.locallens.dto.place.PlaceResponse;
import com.locallens.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/dashboard/summary")
    public ResponseEntity<AdminDashboardResponse>
            getDashboardSummary() {

        return ResponseEntity.ok(
                adminService.getDashboardSummary()
        );
    }

    // =========================================================
    // USER MANAGEMENT
    // =========================================================

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>>
            getAllUsers() {

        return ResponseEntity.ok(
                adminService.getAllUsers()
        );
    }

    @GetMapping("/users/travellers")
    public ResponseEntity<List<AdminUserResponse>>
            getTravellers() {

        return ResponseEntity.ok(
                adminService.getTravellers()
        );
    }

    @GetMapping("/users/local-guides")
    public ResponseEntity<List<AdminUserResponse>>
            getLocalGuides() {

        return ResponseEntity.ok(
                adminService.getLocalGuides()
        );
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<AdminUserResponse>
            updateUserStatus(
                    @PathVariable Long userId,
                    @Valid
                    @RequestBody
                    UpdateUserStatusRequest request
            ) {

        return ResponseEntity.ok(
                adminService.updateUserStatus(
                        userId,
                        request.getActive()
                )
        );
    }

    // =========================================================
    // PLACE MANAGEMENT
    // =========================================================

    @GetMapping("/places/pending")
    public ResponseEntity<List<PlaceResponse>>
            getPendingPlaces() {

        return ResponseEntity.ok(
                adminService.getPendingPlaces()
        );
    }

    @GetMapping("/places/approved")
    public ResponseEntity<List<PlaceResponse>>
            getApprovedPlaces() {

        return ResponseEntity.ok(
                adminService.getApprovedPlaces()
        );
    }

    @GetMapping("/places/rejected")
    public ResponseEntity<List<PlaceResponse>>
            getRejectedPlaces() {

        return ResponseEntity.ok(
                adminService.getRejectedPlaces()
        );
    }

    @PutMapping("/places/{placeId}/approve")
    public ResponseEntity<PlaceResponse>
            approvePlace(
                    @PathVariable Long placeId
            ) {

        return ResponseEntity.ok(
                adminService.approvePlace(placeId)
        );
    }

    @PutMapping("/places/{placeId}/reject")
    public ResponseEntity<PlaceResponse>
            rejectPlace(
                    @PathVariable Long placeId,
                    @RequestParam String reason
            ) {

        return ResponseEntity.ok(
                adminService.rejectPlace(
                        placeId,
                        reason
                )
        );
    }
}