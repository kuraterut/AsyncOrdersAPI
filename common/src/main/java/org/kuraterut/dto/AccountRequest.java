package org.kuraterut.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private Long userId;
    private BigDecimal balance;
}
