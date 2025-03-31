package com.api.basicorder.application;

import com.api.basicorder.core.adapters.in.dto.SaveOrderInput;
import com.api.basicorder.core.domain.Item;
import com.api.basicorder.core.domain.Order;
import com.api.basicorder.core.ports.item.ItemOutputPort;
import com.api.basicorder.core.ports.order.OrderInputPort;
import com.api.basicorder.core.ports.order.OrderOutputPort;
import com.api.basicorder.infrastructure.dynamo.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements OrderInputPort {

    private final OrderOutputPort orderOutputPort;
    private final ItemOutputPort itemOutputPort;
    private final ObjectMapper objectMapper;

    @Override
    public Order getOrder(String id) {
        try {
            return orderOutputPort.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveOrder(SaveOrderInput input) {
        try {
            List<Order.ItemOrder> itemsOrder = new ArrayList<>();

            input.getItems().forEach(item -> {
                Item persistedItem = validateItemExists(item);
                Order.ItemOrder itemOrder = new Order.ItemOrder(item.getItemId(), item.getQuantity(), calculateTotalPeerItem(persistedItem, item.getQuantity()));
                itemsOrder.add(itemOrder);
            });

            Order order = new Order(calculateTotalPeerOrder(itemsOrder), itemsOrder);

            String orderPersistedId = orderOutputPort.save(OrderMapper.toEntity(order));

            orderOutputPort.postOnSqsQueue("order_processed_queue", objectMapper.writeValueAsString(order));

            return orderPersistedId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Double calculateTotalPeerOrder(List<Order.ItemOrder> itemsOrder) {
        return itemsOrder.stream().map(Order.ItemOrder::getTotal).reduce(0.0, Double::sum);
    }

    private Double calculateTotalPeerItem(Item persistedItem, int quantity) {
        return persistedItem.getUnitValue() * quantity;
    }

    private Item validateItemExists(SaveOrderInput.Item item) {
        return itemOutputPort.findById(item.getItemId()).orElseThrow(() -> new RuntimeException("Item not found"));
    }
}
