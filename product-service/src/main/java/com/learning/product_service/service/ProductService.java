package com.learning.product_service.service;

import com.learning.product_service.model.Product;
import com.learning.product_service.repository.ProductRepository;
import com.learning.product_service.request.ProductRequest;
import com.learning.product_service.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            Product product = Product.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .skuCode(productRequest.skuCode())
                    .price(productRequest.price())
                    .build();

            // save entry
            productRepository.save(product);

            // generate a log
            log.info("Product created successfully");

            // return record class
            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getSkuCode(),
                    product.getPrice()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSkuCode(),
                product.getPrice()
        );
    }
}