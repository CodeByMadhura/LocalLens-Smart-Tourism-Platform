package com.locallens.dto.place;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceImageResponse {

    private Long id;

    private String imageUrl;

    private boolean primaryImage;

    private String caption;
}