package com.learning.product_service.response;

import org.bson.types.ObjectId;

import java.math.BigDecimal;

public record ProductResponse(
        ObjectId id,
        String name,
        String description,
        String skuCode,
        BigDecimal price
) { }
