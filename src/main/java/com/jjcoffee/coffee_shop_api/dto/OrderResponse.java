package com.jjcoffee.coffee_shop_api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// DTO for order responses
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String coffeeName;
    private Integer quantity;
    private BigDecimal totalAmount;

}
