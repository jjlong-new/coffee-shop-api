package com.jjcoffee.coffee_shop_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO for creating/updating an order
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Coffee ID is required")
    @Positive(message = "Coffee ID must be a positive integer")
    private Long coffeeId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be >= 1")
    private Integer quantity;

}
