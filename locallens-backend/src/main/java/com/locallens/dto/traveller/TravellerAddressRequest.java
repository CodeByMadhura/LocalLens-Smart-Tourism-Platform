package com.locallens.dto.traveller;

import com.locallens.enums.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravellerAddressRequest {

    @NotBlank(message = "Address line 1 is required")
    @Size(
        max = 150,
        message = "Address line 1 cannot exceed 150 characters"
    )
    private String addressLine1;

    @Size(
        max = 150,
        message = "Address line 2 cannot exceed 150 characters"
    )
    private String addressLine2;

    @Size(
        max = 150,
        message = "Address line 3 cannot exceed 150 characters"
    )
    private String addressLine3;

    @Size(
        max = 100,
        message = "Area cannot exceed 100 characters"
    )
    private String area;

    @NotBlank(message = "City is required")
    @Size(
        max = 100,
        message = "City cannot exceed 100 characters"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(
        max = 100,
        message = "State cannot exceed 100 characters"
    )
    private String state;

    @Size(
        max = 20,
        message = "Zip code cannot exceed 20 characters"
    )
    private String zipCode;

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    private boolean current;
}