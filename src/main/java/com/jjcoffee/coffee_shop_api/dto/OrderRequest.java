package com.jjcoffee.coffee_shop_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// DTO for creating/updating an order
@Data
public class OrderRequest {

    @NotNull(message = "Coffee ID is required")
    @Positive(message = "Coffee ID must be a positive integer")
    private Long coffeeId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be >= 1")
    private int quantity;

}
