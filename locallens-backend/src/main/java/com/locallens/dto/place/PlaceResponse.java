package com.locallens.dto.place;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceResponse {

    private Long id;

    private String name;

    private String city;

    private String state;

    private String category;

    private String description;

    private String foodDescription;

    private BigDecimal estimatedCost;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal averageRating;

    private Integer reviewCount;

    private String status;

    private String rejectionReason;

    private List<PlaceImageResponse> images;

    private GuideSummaryResponse createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}