package com.ecommerce.monolith.customer.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.monolith.customer.dto.CreateCustomerRequest;
import com.ecommerce.monolith.customer.dto.CustomerDTO;
import com.ecommerce.monolith.customer.mapper.CustomerMapper;
import com.ecommerce.monolith.customer.model.Customer;
import com.ecommerce.monolith.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService, CustomerPublicService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return mapper.toDTOList(repository.findAll());
    }
    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        return mapper.toDTO(findCustomer(id));
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        Customer customer = mapper.toEntity(request);
        return mapper.toDTO(repository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = findCustomer(id);
        mapper.updateEntity(request, customer);
        return mapper.toDTO(repository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean customerExists(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO requireCustomer(Long id) {
        return getCustomerById(id);
    }

    private Customer findCustomer(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
    }
}
