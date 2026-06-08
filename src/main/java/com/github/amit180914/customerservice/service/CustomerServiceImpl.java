package com.github.amit180914.customerservice.service;

import com.github.amit180914.customerservice.dto.CustomerResponse;
import com.github.amit180914.customerservice.dto.RegisterCustomerRequest;
import com.github.amit180914.customerservice.dto.UpdateCustomerRequest;
import com.github.amit180914.customerservice.entity.Customer;
import com.github.amit180914.customerservice.exception.DuplicateResourceException;
import com.github.amit180914.customerservice.exception.ResourceNotFoundException;
import com.github.amit180914.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String DEFAULT_STATUS = "ACTIVE";

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse registerCustomer(RegisterCustomerRequest request) {
        validateDuplicateForCreate(request.email(), request.mobileNumber());

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .mobileNumber(request.mobileNumber())
                .email(request.email())
                .status(resolveStatus(request.status()))
                .build();

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerDetails(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerProfile(String customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        if (request.email() != null && !request.email().equals(customer.getEmail())
                && customerRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Customer with email already exists");
        }

        if (request.mobileNumber() != null && !request.mobileNumber().equals(customer.getMobileNumber())
                && customerRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new DuplicateResourceException("Customer with mobile number already exists");
        }

        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.mobileNumber() != null) {
            customer.setMobileNumber(request.mobileNumber());
        }
        if (request.status() != null) {
            customer.setStatus(request.status());
        }

        Customer updated = customerRepository.save(customer);
        return toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validateDuplicateForCreate(String email, String mobileNumber) {
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Customer with email already exists");
        }
        if (customerRepository.existsByMobileNumber(mobileNumber)) {
            throw new DuplicateResourceException("Customer with mobile number already exists");
        }
    }

    private String resolveStatus(String status) {
        return (status == null || status.isBlank()) ? DEFAULT_STATUS : status;
    }

    private CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .mobileNumber(customer.getMobileNumber())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .build();
    }
}

