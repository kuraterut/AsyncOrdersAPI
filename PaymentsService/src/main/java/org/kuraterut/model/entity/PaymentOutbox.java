package org.kuraterut.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.order.PaymentEventResult;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOutbox {
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
    private PaymentEventResult result;

    @Column(nullable = false)
    private boolean published;
}
