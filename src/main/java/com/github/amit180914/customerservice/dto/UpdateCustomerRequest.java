package com.github.amit180914.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        @Pattern(regexp = "^[0-9]{10}$", message = "mobileNumber must be a valid 10-digit number")
        String mobileNumber,
        @Email(message = "email must be valid")
        String email,
        String status
) {
}

