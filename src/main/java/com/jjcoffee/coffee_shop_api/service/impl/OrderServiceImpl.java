package com.jjcoffee.coffee_shop_api.service.impl;

import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.Order;
import com.jjcoffee.coffee_shop_api.exception.ResourceNotFoundException;
import com.jjcoffee.coffee_shop_api.mongo.OrderLogService;
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

    private final OrderLogService orderLogService;
    private final OrderRepository orderRepository;
    private final CoffeeRepository coffeeRepository;
    private final PricingService pricingService;

    // Constructor injection for repositories
    public OrderServiceImpl(OrderRepository orderRepository, CoffeeRepository coffeeRepository,
                                        PricingService pricingService, OrderLogService orderLogService) {
        this.orderRepository = orderRepository;
        this.coffeeRepository = coffeeRepository;
        this.pricingService = pricingService;
        this.orderLogService = orderLogService;
    }

    // Helper method to get price with fallback
    private BigDecimal getBasePrice(Coffee coffee) {
        return pricingService.getPriceForCoffee(coffee.getId())
                .orElseGet(() -> {
                    log.warn("Pricing API failed. Falling back to DB price for coffeeId {}", coffee.getId());

                    return coffee.getPrice(); // Fallback to price stored in the Coffee entity in the database
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
        // Validate the order request for quantity
        if (orderRequest.getQuantity() <= 0) {

            orderLogService.logOrderEvent(
                null, 
                "ORDER_VALIDATION_FAILED", 
                "Quantity must be at least 1"
            );

            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        // Fetch the coffee from the database using the provided coffeeId and handle the case where the coffee is not found
        Coffee coffee = coffeeRepository.findById(orderRequest.getCoffeeId())
                .orElseThrow(() -> {

                    orderLogService.logOrderEvent(
                        null, 
                        "ORDER_VALIDATION_FAILED", 
                        "Coffee not found for coffeeId " + orderRequest.getCoffeeId()
                    );
                    return new ResourceNotFoundException("Coffee not found");
                });

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

        // Log the order creation event in MongoDB
        orderLogService.logOrderEvent(
            savedOrder.getId(), 
            "ORDER_CREATED", 
            "Order successfully created"
        );

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
