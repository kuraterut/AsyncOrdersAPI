package org.kuraterut.mapper;

import org.kuraterut.model.entity.Order;
import org.kuraterut.model.entity.OrderOutbox;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderOutboxMapper {
    public OrderOutbox toOutbox(Order order){
        OrderOutbox orderOutbox = new OrderOutbox();
        orderOutbox.setOrderId(order.getId());
        orderOutbox.setUserId(order.getUserId());
        orderOutbox.setAmount(order.getAmount());
        orderOutbox.setStatus(order.getStatus());
        orderOutbox.setCreatedAt(LocalDateTime.now());
        orderOutbox.setProcessed(false);
        return orderOutbox;
    }
}
