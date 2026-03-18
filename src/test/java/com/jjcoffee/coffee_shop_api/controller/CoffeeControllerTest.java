package com.jjcoffee.coffee_shop_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjcoffee.coffee_shop_api.dto.CoffeeRequest;
import com.jjcoffee.coffee_shop_api.entity.Coffee;
import com.jjcoffee.coffee_shop_api.entity.SizeEnum;
import com.jjcoffee.coffee_shop_api.service.CoffeeService;

@WebMvcTest(CoffeeController.class)
public class CoffeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoffeeService coffeeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Test create coffee
    @Test
    void testCreateCoffee() throws Exception{

        CoffeeRequest coffeeRequest = new CoffeeRequest();
        coffeeRequest.setName("Latte");
        coffeeRequest.setPrice(new BigDecimal("3.50"));
        coffeeRequest.setSize(SizeEnum.MEDIUM);

        when(coffeeService.createCoffee(any())).thenReturn(null);

        mockMvc.perform(post("/coffees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(coffeeRequest)))
            .andExpect(status().isCreated());

        verify(coffeeService).createCoffee(any());
    }

    //Test invalid coffee size
    @Test
    void testCreateCoffee_invalidSize() throws Exception{

        String invalidJson = """
        {
            "name": "Latte",
            "price": 3.50,
            "size": "EXTRA_LARGE"
        }
        """;

        mockMvc.perform(post("/coffees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    //Test Get all coffees
    @Test
    void testGetAllCoffees() throws Exception{

        Coffee coffee1 = new Coffee();
        coffee1.setId(1L);
        coffee1.setName("Latte");
        coffee1.setPrice(new BigDecimal("3.50"));
        coffee1.setSize(SizeEnum.MEDIUM);

        Coffee coffee2 = new Coffee();
        coffee2.setId(2L);
        coffee2.setName("Cappuchino");
        coffee2.setPrice(new BigDecimal("2"));
        coffee2.setSize(SizeEnum.SMALL);

        when(coffeeService.getAllCoffees()).thenReturn(List.of(coffee1,coffee2));

        mockMvc.perform(get("/coffees"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("Latte"))
            .andExpect(jsonPath("$[1].name").value("Cappuchino"));

        verify(coffeeService).getAllCoffees();
    }



}
