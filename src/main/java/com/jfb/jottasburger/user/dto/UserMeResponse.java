package com.jfb.jottasburger.user.dto;

import java.time.Instant;

public record UserMeResponse(
        Long id,
        String name,
        String email,
        String role,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}