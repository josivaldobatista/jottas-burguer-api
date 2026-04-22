package com.jfb.jottasburger.user.dto;

import com.jfb.jottasburger.user.model.Role;

import java.time.Instant;

public record UserMeResponse(
        Long id,
        String name,
        String email,
        Role role,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
