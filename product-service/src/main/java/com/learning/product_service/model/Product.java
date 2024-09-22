package com.learning.product_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "product")
public class Product {
    @Id
    private ObjectId id;
    private String name;
    private String description;
    private String skuCode;
    private BigDecimal price;
}
