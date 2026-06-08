package com.github.amit180914.customerservice.repository;

import com.github.amit180914.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
}

