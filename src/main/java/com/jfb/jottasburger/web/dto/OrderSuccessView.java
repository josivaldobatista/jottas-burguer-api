package com.jfb.jottasburger.web.dto;

import java.math.BigDecimal;

public record OrderSuccessView(
        String orderNumber,
        String status,
        BigDecimal totalAmount
) {
}