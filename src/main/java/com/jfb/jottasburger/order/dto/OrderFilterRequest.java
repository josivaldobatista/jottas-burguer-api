package com.jfb.jottasburger.order.dto;

import com.jfb.jottasburger.order.model.OrderStatus;

public record OrderFilterRequest(
        OrderStatus status
) {
}