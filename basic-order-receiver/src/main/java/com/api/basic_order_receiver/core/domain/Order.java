package com.api.basic_order_receiver.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
public class Order {
    private String id;
    private Double total;
    private List<ItemOrder> itemOrder;

    public Order(Double total, List<ItemOrder> itemOrder) {
        this.id = UUID.randomUUID().toString();
        this.total = total;
        this.itemOrder = itemOrder;
    }

    public Order(String id, Double total, List<ItemOrder> itemOrder) {
        this.id = id;
        this.total = total;
        this.itemOrder = itemOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOrder {
        private String itemId;
        private int quantity;
        private Double total;
    }
}
