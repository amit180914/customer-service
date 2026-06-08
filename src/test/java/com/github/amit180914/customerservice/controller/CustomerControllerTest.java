package com.github.amit180914.customerservice.controller;

import com.github.amit180914.customerservice.dto.CustomerResponse;
import com.github.amit180914.customerservice.dto.RegisterCustomerRequest;
import com.github.amit180914.customerservice.dto.UpdateCustomerRequest;
import com.github.amit180914.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void registerCustomer_shouldReturnCreatedResponse() {
        RegisterCustomerRequest request = RegisterCustomerRequest.builder()
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        when(customerService.registerCustomer(request)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.registerCustomer(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(customerService).registerCustomer(request);
    }

    @Test
    void getCustomerDetails_shouldReturnOkResponse() {
        CustomerResponse response = CustomerResponse.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        when(customerService.getCustomerDetails("cust-1")).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.getCustomerDetails("cust-1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(customerService).getCustomerDetails("cust-1");
    }

    @Test
    void updateCustomerProfile_shouldReturnOkResponse() {
        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .firstName("Amitabh")
                .status("INACTIVE")
                .build();

        CustomerResponse response = CustomerResponse.builder()
                .customerId("cust-1")
                .firstName("Amitabh")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("INACTIVE")
                .build();

        when(customerService.updateCustomerProfile("cust-1", request)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.updateCustomerProfile("cust-1", request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(customerService).updateCustomerProfile("cust-1", request);
    }

    @Test
    void listCustomers_shouldReturnOkResponse() {
        List<CustomerResponse> response = List.of(
                CustomerResponse.builder()
                        .customerId("cust-1")
                        .firstName("Amit")
                        .lastName("Sharma")
                        .email("amit@example.com")
                        .mobileNumber("9999999999")
                        .status("ACTIVE")
                        .build(),
                CustomerResponse.builder()
                        .customerId("cust-2")
                        .firstName("Riya")
                        .lastName("Verma")
                        .email("riya@example.com")
                        .mobileNumber("8888888888")
                        .status("ACTIVE")
                        .build()
        );

        when(customerService.listCustomers()).thenReturn(response);

        ResponseEntity<List<CustomerResponse>> result = customerController.listCustomers();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsExactlyElementsOf(response);
        verify(customerService).listCustomers();
    }
}

