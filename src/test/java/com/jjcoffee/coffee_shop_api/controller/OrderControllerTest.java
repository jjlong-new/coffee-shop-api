package com.jjcoffee.coffee_shop_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;
import com.jjcoffee.coffee_shop_api.service.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JobOperator jobOperator;

    @MockitoBean
    private Job job;

    private ObjectMapper objectMapper = new ObjectMapper();

    //Test successful order creation
    @Test
    void testCreateOrder() throws Exception{

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCoffeeId(1L);
        orderRequest.setQuantity(2);

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(1L)
                .coffeeName("Latte")
                .quantity(2)
                .totalAmount(new BigDecimal("7.00"))
                .build();

        when(orderService.createOrder(any())).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(1L))
            .andExpect(jsonPath("$.coffeeName").value("Latte"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.totalAmount").value(7.00));

        verify(orderService).createOrder(any());
    }

    //Test get order by Id
    @Test
    void testGetOrderById() throws Exception{

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(1L)
                .coffeeName("Latte")
                .quantity(2)
                .totalAmount(new BigDecimal("7.00"))
                .build();

        when(orderService.getOrder(1L)).thenReturn(orderResponse);

        mockMvc.perform(get("/orders/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(1L))
            .andExpect(jsonPath("$.coffeeName").value("Latte"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.totalAmount").value(7.00));

        verify(orderService).getOrder(1L);
    }

}
