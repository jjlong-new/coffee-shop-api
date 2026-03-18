package com.jjcoffee.coffee_shop_api.service;

import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse getOrder(Long orderId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrder(Long id, OrderRequest orderRequest);

    void deleteOrder(Long id);
}
