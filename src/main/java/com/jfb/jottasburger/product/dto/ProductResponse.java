package com.jfb.jottasburger.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean active,
        Long categoryId,
        String categoryName,
        Instant createdAt,
        Instant updatedAt
) {
}