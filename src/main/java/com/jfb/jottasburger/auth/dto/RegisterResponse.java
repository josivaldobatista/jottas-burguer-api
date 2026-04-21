package com.jfb.jottasburger.auth.dto;

public record RegisterResponse(
        Long id,
        String name,
        String email,
        String role
) {
}