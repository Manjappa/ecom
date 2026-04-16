package com.ecom.productservice.product.api;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        Integer availableQuantity
) {
}

