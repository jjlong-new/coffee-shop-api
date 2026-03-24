package com.jjcoffee.coffee_shop_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.Order;
import com.jjcoffee.coffee_shop_api.entity.SizeEnum;
import com.jjcoffee.coffee_shop_api.mongo.OrderLogService;
import com.jjcoffee.coffee_shop_api.repository.CoffeeRepository;
import com.jjcoffee.coffee_shop_api.repository.OrderRepository;
import com.jjcoffee.coffee_shop_api.service.impl.OrderServiceImpl;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLogService orderLogService;

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //Test Create Order successfully
    @Test
    void testCreateOrder(){

        Coffee coffee = new Coffee("Latte", 
                new BigDecimal("3.50"),
                SizeEnum.MEDIUM);

        coffee.setId(1L);

        OrderRequest request = new OrderRequest();
        request.setCoffeeId(1L);
        request.setQuantity(2);

        when(coffeeRepository.findById(1L)).thenReturn(Optional.of(coffee));
        when(pricingService.getPriceForCoffee(1L)).thenReturn(Optional.of(new BigDecimal("3.50")));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderServiceImpl.createOrder(request);

        assertEquals("Latte", response.getCoffeeName());
        assertEquals(2, response.getQuantity());
        assertEquals(new BigDecimal("7.00"), response.getTotalAmount());

        verify(coffeeRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    //Test get order by Id
    @Test
    void testGetOrderById(){

        Coffee coffee = new Coffee("Latte", 
                new BigDecimal("3.50"),
                SizeEnum.MEDIUM);

        coffee.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setCoffee(coffee);
        order.setCoffeeName(coffee.getName());
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("7.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderServiceImpl.getOrder(1L);

        assertEquals("Latte", response.getCoffeeName());
        assertEquals(2, response.getQuantity());
        assertEquals(new BigDecimal("7.00"), response.getTotalAmount());

        verify(orderRepository, times(1)).findById(1L);
    }
}
