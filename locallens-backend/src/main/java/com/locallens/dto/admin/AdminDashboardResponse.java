package com.locallens.dto.admin;

import java.util.List;

import com.locallens.dto.place.PlaceResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalTravellers;
    private long totalLocalGuides;
    private long activeUsers;

    private long pendingPlaces;
    private long approvedPlaces;
    private long rejectedPlaces;

    private List<AdminUserResponse> latestUsers;
    private List<PlaceResponse> latestPlaces;
}