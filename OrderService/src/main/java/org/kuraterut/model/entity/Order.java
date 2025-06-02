package org.kuraterut.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.order.OrderStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
