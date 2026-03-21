package com.ecommerce.monolith.order.service;

import java.util.List;

import com.ecommerce.monolith.order.dto.CreateOrderRequest;
import com.ecommerce.monolith.order.dto.OrderDTO;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id);

    OrderDTO createOrder(CreateOrderRequest request);

    List<OrderDTO> getCustomerOrderHistory(Long customerId);
}
