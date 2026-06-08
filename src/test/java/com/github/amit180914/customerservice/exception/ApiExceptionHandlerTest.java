package com.github.amit180914.customerservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

    @Test
    void handleResourceNotFound_shouldReturn404() {
        ResponseEntity<Map<String, Object>> response = apiExceptionHandler
                .handleResourceNotFound(new ResourceNotFoundException("not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", "not found");
    }

    @Test
    void handleDuplicateResource_shouldReturn409() {
        ResponseEntity<Map<String, Object>> response = apiExceptionHandler
                .handleDuplicateResource(new DuplicateResourceException("already exists"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("message", "already exists");
    }
}

