package com.jjcoffee.coffee_shop_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.Order;
import com.jjcoffee.coffee_shop_api.entity.SizeEnum;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoffeeRepository coffeeRepository;

    //Test save order
    @Test
    void testSaveOrder(){

        Coffee coffee = new Coffee("Latte", 
            new BigDecimal("3.50"), 
            SizeEnum.MEDIUM);
        Coffee savedCoffee = coffeeRepository.save(coffee);

        Order order = new Order();
        order.setCoffee(savedCoffee);
        order.setCoffeeName(savedCoffee.getName());
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("7.00"));
        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedCoffee.getName()).isEqualTo("Latte");
        assertThat(savedOrder.getQuantity()).isEqualTo(2);
        assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo(new BigDecimal("7.00"));
    }

    //Test get order by Id
    @Test
    void testGetOrderById(){

        Coffee coffee = new Coffee("Latte", 
            new BigDecimal("3.50"), 
            SizeEnum.MEDIUM);
        Coffee savedCoffee = coffeeRepository.save(coffee);

        Order order = new Order();
        order.setCoffee(savedCoffee);
        order.setCoffeeName(savedCoffee.getName());
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("7.00"));
        Order savedOrder = orderRepository.save(order);

        var foundOrder = orderRepository.findById(savedOrder.getId());

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getCoffee().getName()).isEqualTo("Latte");
        assertThat(foundOrder.get().getQuantity()).isEqualTo(2);
        assertThat(foundOrder.get().getTotalAmount()).isEqualByComparingTo(new BigDecimal("7.00"));
    }

}
