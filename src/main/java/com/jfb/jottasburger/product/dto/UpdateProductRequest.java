package com.jfb.jottasburger.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name must have at most 150 characters")
        String name,

        @NotBlank(message = "Product description is required")
        @Size(max = 500, message = "Product description must have at most 500 characters")
        String description,

        @NotNull(message = "Product price is required")
        @DecimalMin(value = "0.01", message = "Product price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Category id is required")
        Long categoryId
) {
}