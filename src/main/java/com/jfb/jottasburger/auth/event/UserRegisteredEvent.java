package com.jfb.jottasburger.auth.event;

public record UserRegisteredEvent(
        Long userId,
        String name,
        String email
) {
}