package com.api.basicorder.core.adapters.in.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveOrderInput {

    private List<Item> items;

    @Data
    public static class Item {
        private String itemId;
        private int quantity;
    }
}
