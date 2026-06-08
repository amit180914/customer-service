package com.github.amit180914.customerservice.service;

import com.github.amit180914.customerservice.dto.CustomerResponse;
import com.github.amit180914.customerservice.dto.RegisterCustomerRequest;
import com.github.amit180914.customerservice.dto.UpdateCustomerRequest;
import com.github.amit180914.customerservice.entity.Customer;
import com.github.amit180914.customerservice.exception.DuplicateResourceException;
import com.github.amit180914.customerservice.exception.ResourceNotFoundException;
import com.github.amit180914.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void registerCustomer_shouldCreateCustomer_whenEmailAndMobileAreUnique() {
        RegisterCustomerRequest request = RegisterCustomerRequest.builder()
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .build();

        Customer saved = Customer.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        when(customerRepository.existsByEmail("amit@example.com")).thenReturn(false);
        when(customerRepository.existsByMobileNumber("9999999999")).thenReturn(false);
        when(customerRepository.save(org.mockito.ArgumentMatchers.any(Customer.class))).thenReturn(saved);

        CustomerResponse response = customerService.registerCustomer(request);

        assertThat(response.customerId()).isEqualTo("cust-1");
        assertThat(response.firstName()).isEqualTo("Amit");
        assertThat(response.lastName()).isEqualTo("Sharma");
        assertThat(response.email()).isEqualTo("amit@example.com");
        assertThat(response.mobileNumber()).isEqualTo("9999999999");
        assertThat(response.status()).isEqualTo("ACTIVE");
        verify(customerRepository).save(org.mockito.ArgumentMatchers.any(Customer.class));
    }

    @Test
    void registerCustomer_shouldThrowDuplicateResourceException_whenEmailExists() {
        RegisterCustomerRequest request = RegisterCustomerRequest.builder()
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .build();

        when(customerRepository.existsByEmail("amit@example.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.registerCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");

        verify(customerRepository, never()).save(org.mockito.ArgumentMatchers.any(Customer.class));
    }

    @Test
    void registerCustomer_shouldThrowDuplicateResourceException_whenMobileExists() {
        RegisterCustomerRequest request = RegisterCustomerRequest.builder()
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .build();

        when(customerRepository.existsByEmail("amit@example.com")).thenReturn(false);
        when(customerRepository.existsByMobileNumber("9999999999")).thenReturn(true);

        assertThatThrownBy(() -> customerService.registerCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("mobile");

        verify(customerRepository, never()).save(org.mockito.ArgumentMatchers.any(Customer.class));
    }

    @Test
    void getCustomerDetails_shouldReturnCustomer_whenCustomerExists() {
        Customer existing = Customer.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(existing));

        CustomerResponse response = customerService.getCustomerDetails("cust-1");

        assertThat(response.customerId()).isEqualTo("cust-1");
        assertThat(response.firstName()).isEqualTo("Amit");
        assertThat(response.lastName()).isEqualTo("Sharma");
        assertThat(response.email()).isEqualTo("amit@example.com");
        assertThat(response.mobileNumber()).isEqualTo("9999999999");
        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    void getCustomerDetails_shouldThrowResourceNotFoundException_whenCustomerDoesNotExist() {
        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerDetails("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void updateCustomerProfile_shouldUpdateProvidedFields_only() {
        Customer existing = Customer.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .firstName("Amitabh")
                .status("INACTIVE")
                .build();

        Customer saved = Customer.builder()
                .customerId("cust-1")
                .firstName("Amitabh")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("INACTIVE")
                .build();

        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(saved);

        CustomerResponse response = customerService.updateCustomerProfile("cust-1", request);

        assertThat(response.firstName()).isEqualTo("Amitabh");
        assertThat(response.lastName()).isEqualTo("Sharma");
        assertThat(response.status()).isEqualTo("INACTIVE");
    }

    @Test
    void updateCustomerProfile_shouldThrowResourceNotFoundException_whenCustomerDoesNotExist() {
        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .firstName("Amitabh")
                .build();

        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomerProfile("missing", request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");

        verify(customerRepository, never()).save(org.mockito.ArgumentMatchers.any(Customer.class));
    }

    @Test
    void listCustomers_shouldReturnAllCustomers() {
        Customer c1 = Customer.builder()
                .customerId("cust-1")
                .firstName("Amit")
                .lastName("Sharma")
                .email("amit@example.com")
                .mobileNumber("9999999999")
                .status("ACTIVE")
                .build();

        Customer c2 = Customer.builder()
                .customerId("cust-2")
                .firstName("Riya")
                .lastName("Verma")
                .email("riya@example.com")
                .mobileNumber("8888888888")
                .status("ACTIVE")
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerResponse> response = customerService.listCustomers();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).customerId()).isEqualTo("cust-1");
        assertThat(response.get(1).customerId()).isEqualTo("cust-2");
    }
}

