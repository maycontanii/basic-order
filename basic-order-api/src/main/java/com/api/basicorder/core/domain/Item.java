package com.api.basicorder.core.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Item {
    private String id;
    private String title;
    private Double unitValue;

    public Item() {
    }

    public Item(String title, Double unitValue) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.unitValue = unitValue;
    }

    public Item(String id, String title, Double unitValue) {
        this.id = id;
        this.title = title;
        this.unitValue = unitValue;
    }
}
