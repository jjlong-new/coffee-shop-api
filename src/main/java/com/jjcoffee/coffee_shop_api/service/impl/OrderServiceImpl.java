package com.jjcoffee.coffee_shop_api.service.impl;

import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.Order;
import com.jjcoffee.coffee_shop_api.exception.ResourceNotFoundException;
import com.jjcoffee.coffee_shop_api.repository.CoffeeRepository;
import com.jjcoffee.coffee_shop_api.repository.OrderRepository;
import com.jjcoffee.coffee_shop_api.service.OrderService;
import com.jjcoffee.coffee_shop_api.service.PricingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CoffeeRepository coffeeRepository;
    private final PricingService pricingService;

    // Constructor injection for repositories
    public OrderServiceImpl(OrderRepository orderRepository, CoffeeRepository coffeeRepository, PricingService pricingService) {
        this.orderRepository = orderRepository;
        this.coffeeRepository = coffeeRepository;
        this.pricingService = pricingService;
    }

    // Helper method to get price with fallback
    private BigDecimal getBasePrice(Coffee coffee) {
        return pricingService.getPriceForCoffee(coffee.getId())
                .orElseGet(() -> {
                    log.warn("Pricing API failed. Falling back to DB price for coffeeId {}", coffee.getId());
                    return coffee.getPrice();
                });
    }

    // Helper method to map Order entity to OrderResponse DTO
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .coffeeName(order.getCoffeeName())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .build();
    }


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        Coffee coffee = coffeeRepository.findById(orderRequest.getCoffeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Coffee not found"));

        BigDecimal basePrice = getBasePrice(coffee);

        BigDecimal totalPrice =
                basePrice.multiply(BigDecimal.valueOf(orderRequest.getQuantity()));

        // Create an Order entity, set its properties
        Order order = Order.builder()
                .coffee(coffee)
                .coffeeName(coffee.getName())
                .quantity(orderRequest.getQuantity())
                .totalAmount(totalPrice)
                .build();

        // Save the order to the database
        Order savedOrder = orderRepository.save(order);

        // Create and return an OrderResponse DTO
        return mapToResponse(savedOrder);
    }


    @Override
    public OrderResponse getOrder(Long orderId) {
        // Fetch the order from the database
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Create and return an OrderResponse DTO
        return mapToResponse(order);
    }


    @Override
    public List<OrderResponse> getAllOrders() {
        // Fetch all orders from the database
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Coffee coffee = coffeeRepository.findById(orderRequest.getCoffeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Coffee not found"));

        BigDecimal basePrice = getBasePrice(coffee);

        BigDecimal totalPrice =
                basePrice.multiply(BigDecimal.valueOf(orderRequest.getQuantity()));
        
        order.setCoffee(coffee);
        order.setCoffeeName(coffee.getName());
        order.setQuantity(orderRequest.getQuantity());
        order.setTotalAmount(totalPrice);

        return mapToResponse(orderRepository.save(order));
    }


    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderRepository.delete(order);
    }
}
