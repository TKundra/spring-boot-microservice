package com.learning.inventory_service;

import com.learning.inventory_service.model.Inventory;
import com.learning.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	/**
	 * At the time of Application Start-up, this bean will be created.
	 * Create both the inventories and save to the database
	 * */
	// load at data at the time of application start-up
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory.setSkuCode("iphone_13_red");
			inventory.setQuantity(10);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
