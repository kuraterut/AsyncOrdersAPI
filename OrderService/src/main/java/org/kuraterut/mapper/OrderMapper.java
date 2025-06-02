package org.kuraterut.mapper;

import org.kuraterut.dto.OrderRequest;
import org.kuraterut.dto.OrderResponse;
import org.kuraterut.model.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toEntity(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setAmount(orderRequest.getAmount());
        order.setDescription(orderRequest.getDescription());
        return order;
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setAmount(order.getAmount());
        orderResponse.setDescription(order.getDescription());
        orderResponse.setStatus(order.getStatus());
        return orderResponse;
    }
}
