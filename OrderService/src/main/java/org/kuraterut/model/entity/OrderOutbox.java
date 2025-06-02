package org.kuraterut.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed", nullable = false)
    private boolean processed;

}