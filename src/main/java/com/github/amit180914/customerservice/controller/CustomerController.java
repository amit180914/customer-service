package com.github.amit180914.customerservice.controller;

import com.github.amit180914.customerservice.dto.CustomerResponse;
import com.github.amit180914.customerservice.dto.RegisterCustomerRequest;
import com.github.amit180914.customerservice.dto.UpdateCustomerRequest;
import com.github.amit180914.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        CustomerResponse response = customerService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerDetails(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomerDetails(customerId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomerProfile(
            @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(customerService.updateCustomerProfile(customerId, request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }
}

