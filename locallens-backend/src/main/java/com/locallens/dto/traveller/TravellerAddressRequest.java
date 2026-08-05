package com.locallens.dto.traveller;

import com.locallens.enums.AddressType;

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

    @Size(
        max = 100,
        message = "City cannot exceed 100 characters"
    )
    private String city;

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

    /*
     * This is optional because the service explicitly supplies
     * PERMANENT or CURRENT while saving the address.
     */
    private AddressType addressType;

    /*
     * Retained for compatibility with older profile requests.
     */
    private boolean current;
}