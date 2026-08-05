package com.locallens.dto.guide;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotBlank
    private String addressLine1;

    private String addressLine2;
    private String addressLine3;
    private String area;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    private String zipCode;
}