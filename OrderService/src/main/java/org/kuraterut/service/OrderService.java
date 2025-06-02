package org.kuraterut.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.kuraterut.exception.exceptions.OrderNotFoundException;
import org.kuraterut.mapper.OrderMapper;
import org.kuraterut.mapper.OrderOutboxMapper;
import org.kuraterut.dto.OrderRequest;
import org.kuraterut.dto.OrderResponse;
import org.kuraterut.model.entity.Order;
import org.kuraterut.model.entity.OrderOutbox;
import org.kuraterut.order.OrderStatus;
import org.kuraterut.order.PaymentEventResult;
import org.kuraterut.order.PaymentResult;
import org.kuraterut.repository.OrderOutboxRepository;
import org.kuraterut.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;
    private final OrderMapper orderMapper;
    private final OrderOutboxMapper orderOutboxMapper;


    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        order.setStatus(OrderStatus.NEW);
        order = orderRepository.save(order);

        OrderOutbox orderOutbox = orderOutboxMapper.toOutbox(order);
        orderOutboxRepository.save(orderOutbox);

        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found by ID: " + orderId));
        return orderMapper.toResponse(order);
    }


    public List<OrderResponse> getByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenPaymentResult(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentResult paymentResult = objectMapper.readValue(message, PaymentResult.class);
        Order order = orderRepository.findById(paymentResult.getOrderId())
                .orElse(new Order());
        if(paymentResult.getResult() == PaymentEventResult.SUCCESS){
            order.setStatus(OrderStatus.FINISHED);
        }
        else{
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
    }

}
