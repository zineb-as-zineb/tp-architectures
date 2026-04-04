package com.ecommerce.monolith.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ecommerce.monolith.order.model.OrderStatus;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
