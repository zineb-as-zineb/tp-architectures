package com.ecommerce.monolith.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.monolith.customer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
