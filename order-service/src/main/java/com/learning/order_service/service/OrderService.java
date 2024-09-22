package com.learning.order_service.service;

import com.learning.order_service.config.WebClientConfig;
import com.learning.order_service.dto.OrderLineItemsDto;
import com.learning.order_service.event.OrderPlacedEvent;
import com.learning.order_service.model.Order;
import com.learning.order_service.model.OrderLineItems;
import com.learning.order_service.repository.OrderRepository;
import com.learning.order_service.request.OrderRequest;
import com.learning.order_service.response.InventoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClientConfig webClient;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public ResponseEntity<String> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDto).collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItems);

        // get all skuCodes from order
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        // call inventory service and place order if it is in stock
        InventoryResponse[] inventoryResponses = webClient.webClientBuilder().build().get()
                .uri(
                        "http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block(); // block make sync request

        // products are in stock
        boolean isAllProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (isAllProductsInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return ResponseEntity.status(HttpStatus.OK).body("Order Placed");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product is not in stock");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
