package com.jjcoffee.coffee_shop_api.dto;

import com.jjcoffee.coffee_shop_api.entity.SizeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
//import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// DTO for creating/updating coffee items
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeRequest {

    @NotBlank(message = "Coffee name is required")
    @Size(min = 5, message = "Coffee name must be at least 5 characters long")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value greater than zero")
    private BigDecimal price;

    //@Pattern(regexp = "SMALL|MEDIUM|LARGE", message = "Size must be SMALL, MEDIUM, or LARGE")
    @NotNull(message = "Size is required. Allowed values = SMALL, MEDIUM, LARGE")
    private SizeEnum size;

}
