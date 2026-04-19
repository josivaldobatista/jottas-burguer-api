package com.jfb.jottasburger.order.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class DefaultOrderNumberGenerator implements OrderNumberGenerator {

    @Override
    public String generate() {
        return "ORD-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}