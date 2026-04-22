package com.jfb.jottasburger.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserMeRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must have at most 120 characters")
        String name
) {
}