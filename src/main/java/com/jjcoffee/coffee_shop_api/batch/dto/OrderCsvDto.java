package com.jjcoffee.coffee_shop_api.batch.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderCsvDto {
    private Long orderId;
    private Integer storeId;
    private Long customerId;
    private String beverageName;
    private String size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Boolean discountApplied;
    private LocalDateTime orderTimestamp;
}
