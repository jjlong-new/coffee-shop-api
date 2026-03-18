package com.jjcoffee.coffee_shop_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jjcoffee.coffee_shop_api.dto.CoffeeRequest;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.SizeEnum;
import com.jjcoffee.coffee_shop_api.repository.CoffeeRepository;
import com.jjcoffee.coffee_shop_api.service.impl.CoffeeServiceImpl;

public class CoffeeServiceTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @InjectMocks
    private CoffeeServiceImpl coffeeServiceImpl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //Test Create Coffee
    @Test
    void testCreateCoffee(){

        CoffeeRequest coffeeRequest = new CoffeeRequest();
        coffeeRequest.setName("Latte");
        coffeeRequest.setPrice(new BigDecimal("3.50"));
        coffeeRequest.setSize(SizeEnum.MEDIUM);

        Coffee coffee = new Coffee("Latte", 
                new BigDecimal("3.50"),
                SizeEnum.MEDIUM);

        when(coffeeRepository.save(any(Coffee.class))).thenReturn(coffee);

        Coffee result = coffeeServiceImpl.createCoffee(coffeeRequest);

        assertEquals("Latte", result.getName());
        assertEquals(new BigDecimal("3.50"), result.getPrice());
        assertEquals(SizeEnum.MEDIUM, result.getSize());

        verify(coffeeRepository, times(1)).save(any(Coffee.class));
    }

    //Test get all coffees
    @Test
    void testGetAllCoffees(){

        Coffee coffee1 = new Coffee("Latte", 
                new BigDecimal("3.50"),
                SizeEnum.MEDIUM);

        Coffee coffee2 = new Coffee("Espresso", 
                new BigDecimal("4.50"),
                SizeEnum.SMALL);

        when(coffeeRepository.findAll()).thenReturn(List.of(coffee1, coffee2));

        List<Coffee> result = coffeeServiceImpl.getAllCoffees();

        assertEquals(2, result.size());
        assertEquals("Latte", result.get(0).getName());
        assertEquals("Espresso", result.get(1).getName());

        verify(coffeeRepository, times(1)).findAll();
    }

}
