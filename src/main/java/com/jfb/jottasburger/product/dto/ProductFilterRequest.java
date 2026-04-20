package com.jfb.jottasburger.product.dto;

public record ProductFilterRequest(
        String name,
        Boolean active,
        Long categoryId
) {
}
