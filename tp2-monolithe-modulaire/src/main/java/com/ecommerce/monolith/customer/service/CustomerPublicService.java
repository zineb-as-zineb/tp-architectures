package com.ecommerce.monolith.customer.service;

import com.ecommerce.monolith.customer.dto.CustomerDTO;

public interface CustomerPublicService {
    boolean customerExists(Long id);
    CustomerDTO requireCustomer(Long id);
}
