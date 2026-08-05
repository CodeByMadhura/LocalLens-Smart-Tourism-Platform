package com.locallens.dto.guide;

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
public class AddressResponse {

    private Long id;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;

    private String area;

    private String city;

    private String state;

    private String zipCode;

    private String addressType;

    private boolean current;
}