package com.ecommerce.monolith.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecommerce.monolith.order.dto.OrderDTO;
import com.ecommerce.monolith.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "productName", ignore = true)
    OrderDTO toDTO(Order order);

    List<OrderDTO> toDTOList(List<Order> orders);
}
