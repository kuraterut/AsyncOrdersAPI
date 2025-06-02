package org.kuraterut.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.order.OrderStatus;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String description;
    private OrderStatus status;
}
