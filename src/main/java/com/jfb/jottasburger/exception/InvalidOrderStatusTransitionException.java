package com.jfb.jottasburger.exception;

public class InvalidOrderStatusTransitionException extends BusinessException {

    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }
}