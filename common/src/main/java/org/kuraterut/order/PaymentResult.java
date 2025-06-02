package org.kuraterut.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {
    private Long orderId;
    private Long userId;
    private PaymentEventResult result;
}
