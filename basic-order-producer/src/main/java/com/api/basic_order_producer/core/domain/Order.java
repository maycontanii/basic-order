package com.api.basic_order_producer.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {

    private List<Item> items;

    @Data
    @AllArgsConstructor
    public static class Item {
        private String itemId;
        private int quantity;
    }
}
