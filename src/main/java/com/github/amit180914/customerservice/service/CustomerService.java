package com.github.amit180914.customerservice.service;

import com.github.amit180914.customerservice.dto.CustomerResponse;
import com.github.amit180914.customerservice.dto.RegisterCustomerRequest;
import com.github.amit180914.customerservice.dto.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {

    CustomerResponse registerCustomer(RegisterCustomerRequest request);

    CustomerResponse getCustomerDetails(String customerId);

    CustomerResponse updateCustomerProfile(String customerId, UpdateCustomerRequest request);

    List<CustomerResponse> listCustomers();
}

