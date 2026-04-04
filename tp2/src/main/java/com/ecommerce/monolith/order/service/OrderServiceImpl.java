package com.ecommerce.monolith.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.monolith.customer.dto.CustomerDTO;
import com.ecommerce.monolith.customer.service.CustomerPublicService;
import com.ecommerce.monolith.order.dto.CreateOrderRequest;
import com.ecommerce.monolith.order.dto.OrderDTO;
import com.ecommerce.monolith.order.mapper.OrderMapper;
import com.ecommerce.monolith.order.model.Order;
import com.ecommerce.monolith.order.repository.OrderRepository;
import com.ecommerce.monolith.product.dto.ProductDTO;
import com.ecommerce.monolith.product.service.ProductPublicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ProductPublicService productPublicService;
    private final CustomerPublicService customerPublicService;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return enrich(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        return enrich(findOrder(id));
    }

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        CustomerDTO customer = customerPublicService.requireCustomer(request.getCustomerId());
        ProductDTO product = productPublicService.requireProduct(request.getProductId());

        Order order = Order.builder()
                .customerId(customer.getId())
                .productId(product.getId())
                .quantity(request.getQuantity())
                .unitPrice(product.getPrice())
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();

        return enrich(repository.save(order), customer, product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getCustomerOrderHistory(Long customerId) {
        customerPublicService.requireCustomer(customerId);
        return enrich(repository.findByCustomerIdOrderByCreatedAtDesc(customerId));
    }

    private Order findOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    private List<OrderDTO> enrich(List<Order> orders) {
        return orders.stream().map(this::enrich).toList();
    }

    private OrderDTO enrich(Order order) {
        CustomerDTO customer = customerPublicService.requireCustomer(order.getCustomerId());
        ProductDTO product = productPublicService.requireProduct(order.getProductId());
        return enrich(order, customer, product);
    }

    private OrderDTO enrich(Order order, CustomerDTO customer, ProductDTO product) {
        OrderDTO dto = mapper.toDTO(order);
        dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        dto.setProductName(product.getName());
        return dto;
    }
}
