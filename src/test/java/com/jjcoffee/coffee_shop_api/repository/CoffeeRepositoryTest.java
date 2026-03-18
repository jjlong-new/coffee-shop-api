package com.jjcoffee.coffee_shop_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.SizeEnum;

@DataJpaTest
public class CoffeeRepositoryTest {

    @Autowired
    private CoffeeRepository coffeeRepository;

    //Test save coffee
    @Test
    void testSaveCoffee(){

        Coffee coffee = new Coffee();
        coffee.setName("Latte");
        coffee.setPrice(new BigDecimal("3.50"));
        coffee.setSize(SizeEnum.MEDIUM);

        Coffee savedCoffee = coffeeRepository.save(coffee);

        assertThat(savedCoffee.getId()).isNotNull();
        assertThat(savedCoffee.getName()).isEqualTo("Latte");
        assertThat(savedCoffee.getPrice()).isEqualByComparingTo(new BigDecimal("3.50"));
        assertThat(savedCoffee.getSize()).isEqualTo(SizeEnum.MEDIUM);
    }

    //Test find all coffees
    @Test
    void testGetAllCoffees(){

        Coffee coffee1 = new Coffee();
        coffee1.setName("Latte");
        coffee1.setPrice(new BigDecimal("3.50"));
        coffee1.setSize(SizeEnum.MEDIUM);

        Coffee coffee2 = new Coffee();
        coffee2.setName("Espresso");
        coffee2.setPrice(new BigDecimal("4.50"));
        coffee2.setSize(SizeEnum.SMALL);

        coffeeRepository.save(coffee1);
        coffeeRepository.save(coffee2);

        List<Coffee> coffees = coffeeRepository.findAll();

        assertThat(coffees).hasSize(2);
    }



}
