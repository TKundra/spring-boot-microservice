package com.learning.inventory_service.repository;

import com.learning.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // find all inventories with provided skuCodes
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
