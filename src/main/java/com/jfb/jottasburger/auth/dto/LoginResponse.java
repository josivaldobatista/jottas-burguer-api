package com.jfb.jottasburger.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}