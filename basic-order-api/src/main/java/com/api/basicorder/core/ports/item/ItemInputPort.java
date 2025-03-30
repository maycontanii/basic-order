package com.api.basicorder.core.ports.item;

import com.api.basicorder.core.domain.Item;

import java.util.List;

public interface ItemInputPort {
    Item getItem(String id);
    List<Item> getItems();
}
