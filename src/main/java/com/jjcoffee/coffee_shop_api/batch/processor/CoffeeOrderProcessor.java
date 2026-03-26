package com.jjcoffee.coffee_shop_api.batch.processor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.jjcoffee.coffee_shop_api.batch.dto.*;

@Component
public class CoffeeOrderProcessor implements ItemProcessor<OrderBatchDto, OrderCsvDto> {

    @Override
    public OrderCsvDto process(OrderBatchDto orderBatchDto) {

        if (orderBatchDto.getUnitPrice() == null || orderBatchDto.getQuantity() == null || orderBatchDto.getQuantity() <= 0) {
            throw new RuntimeException("Invalid record");
        }

        BigDecimal totalPrice = orderBatchDto.getUnitPrice().multiply(BigDecimal.valueOf(orderBatchDto.getQuantity()));

        String normalized = capitalize(orderBatchDto.getBeverageName());

        boolean discount = orderBatchDto.getQuantity() >= 3;

        return OrderCsvDto.builder()
            .orderId(orderBatchDto.getOrderId())
            .storeId(orderBatchDto.getStoreId())
            .customerId(orderBatchDto.getCustomerId())
            .beverageName(normalized)
            .size(orderBatchDto.getSize())
            .quantity(orderBatchDto.getQuantity())
            .unitPrice(orderBatchDto.getUnitPrice())
            .totalPrice(totalPrice)
            .discountApplied(discount)
            .orderTimestamp(orderBatchDto.getOrderTimestamp())
            .build();
    }

    private String capitalize(String input) {
        if (input == null) return null;
        return Arrays.stream(input.toLowerCase().split(" "))
                .filter(w -> !w.isBlank())
                .map(w -> w.substring(0,1).toUpperCase() + w.substring(1))
                .collect(Collectors.joining(" "));
    }

}
