package com.github.amit180914.customerservice.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String customerId,
        String firstName,
        String lastName,
        String mobileNumber,
        String email,
        String status
) {
}

