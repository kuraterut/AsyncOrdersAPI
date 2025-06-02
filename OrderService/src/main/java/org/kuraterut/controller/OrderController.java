package org.kuraterut.controller;

import lombok.RequiredArgsConstructor;
import org.kuraterut.dto.OrderRequest;
import org.kuraterut.dto.OrderResponse;
import org.kuraterut.order.OrderStatus;
import org.kuraterut.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(orderService.getByUserId(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }


}
