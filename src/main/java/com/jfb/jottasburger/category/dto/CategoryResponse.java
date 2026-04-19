package com.jfb.jottasburger.category.dto;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}