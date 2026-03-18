package com.jjcoffee.coffee_shop_api.mongo;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class OrderLogService {

    private final OrderLogRepository orderLogRepository;

    public OrderLogService(OrderLogRepository orderLogRepository) {
        this.orderLogRepository = orderLogRepository;
    }

    public void logOrderEvent(Long orderId, String eventType, String details) {
        OrderLog log = OrderLog.builder()
                .orderId(orderId)
                .timestamp(Instant.now())
                .eventType(eventType)
                .details(details)
                .build();
        orderLogRepository.save(log);
    }



}
