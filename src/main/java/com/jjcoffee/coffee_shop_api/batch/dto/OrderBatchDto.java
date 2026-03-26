package com.jjcoffee.coffee_shop_api.batch.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderBatchDto {
    private Long orderId;
    private Integer storeId;
    private Long customerId;
    private String beverageName;
    private String size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDateTime orderTimestamp;
}
