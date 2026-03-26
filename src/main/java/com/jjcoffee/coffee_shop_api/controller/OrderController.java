package com.jjcoffee.coffee_shop_api.controller;


import com.jjcoffee.coffee_shop_api.dto.OrderRequest;
import com.jjcoffee.coffee_shop_api.dto.OrderResponse;
import com.jjcoffee.coffee_shop_api.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.batch.core.job.*;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final JobOperator jobOperator;

    private final Job job;

    public OrderController(OrderService orderService, JobOperator jobOperator, Job job) {
        this.orderService = orderService;
        this.jobOperator = jobOperator;
        this.job = job;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/{id}")
    public OrderResponse updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);

        return ResponseEntity.ok("Order deleted successfully");
    }

    @PostMapping("/batch/export-coffee-sales")
    public ResponseEntity<?> export(@RequestBody Map<String, Object> request) throws Exception {

        if (!request.containsKey("startDate") ||
            !request.containsKey("endDate") ||
            !request.containsKey("storeId") ||
            !request.containsKey("outputFilePath")) {

            return ResponseEntity.badRequest().body("Missing required fields");
        }

        JobParameters params = new JobParametersBuilder()
            .addString("startDate", request.get("startDate").toString())
            .addString("endDate", request.get("endDate").toString())
            .addLong("storeId", Long.parseLong(request.get("storeId").toString()))
            .addString("outputFilePath", request.get("outputFilePath").toString())
            .addLong("runDate", System.currentTimeMillis())
            .toJobParameters();

        JobExecution execution = jobOperator.start(job, params);

        return ResponseEntity.ok(Map.of(
                "executionId", execution.getId(),
                "status", execution.getStatus()
        ));
    }
}
