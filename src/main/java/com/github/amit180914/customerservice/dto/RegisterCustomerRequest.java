package com.github.amit180914.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegisterCustomerRequest(
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "mobileNumber is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "mobileNumber must be a valid 10-digit number")
        String mobileNumber,
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,
        String status
) {
}

