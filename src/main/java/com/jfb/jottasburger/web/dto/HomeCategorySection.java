package com.jfb.jottasburger.web.dto;

import com.jfb.jottasburger.product.dto.ProductResponse;

import java.util.List;

public record HomeCategorySection(
        Long id,
        String name,
        List<ProductResponse> products
) {
}