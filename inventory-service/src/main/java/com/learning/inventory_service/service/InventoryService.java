package com.learning.inventory_service.service;

import com.learning.inventory_service.repository.InventoryRepository;
import com.learning.inventory_service.response.InventoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional()
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        log.info("Checking inventory repository");
        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory -> InventoryResponse
                        .builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()
                ).collect(Collectors.toList());
    }
}
