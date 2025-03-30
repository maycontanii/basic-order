package com.api.basicorder.application;

import com.api.basicorder.core.domain.Item;
import com.api.basicorder.core.ports.item.ItemInputPort;
import com.api.basicorder.core.ports.item.ItemOutputPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService implements ItemInputPort {

    private final ItemOutputPort itemOutputPort;

    @Override
    public Item getItem(String id) {
        return itemOutputPort.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Item> getItems() {
        return itemOutputPort.findAll();
    }
}
