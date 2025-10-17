package com.shopping.order_service.service;

import com.shopping.order_service.client.InventoryClient;
import com.shopping.order_service.model.Order;
import com.shopping.order_service.repository.OrderRepository;
import com.shopping.order_service.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(!isProductInStock){
            throw new RuntimeException("Product with sku code " + orderRequest.skuCode() + " is not in stock");
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setSkuCode(orderRequest.skuCode());
        order.setQuantity(orderRequest.quantity());

        orderRepository.save(order);
    }

}
