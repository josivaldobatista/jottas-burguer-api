package com.jfb.jottasburger.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}