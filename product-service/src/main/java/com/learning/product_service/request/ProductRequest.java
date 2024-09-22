package com.learning.product_service.request;

import org.bson.types.ObjectId;

import java.math.BigDecimal;

/**
 * Records are final classes themselves. However, record classes have more constraints compared to regular final classes.
 * On the other hand, records are more convenient to use in certain situations since they can be declared in a single
 * line, and the compiler automatically generates everything else.
 * */
public record ProductRequest(
        ObjectId id,
        String name,
        String description,
        String skuCode,
        BigDecimal price
) { }
