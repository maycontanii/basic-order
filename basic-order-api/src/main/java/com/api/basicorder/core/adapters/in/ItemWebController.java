package com.api.basicorder.core.adapters.in;

import com.api.basicorder.core.domain.Item;
import com.api.basicorder.core.ports.item.ItemInputPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemWebController {

    private final ItemInputPort itemInputPort;

    @GetMapping({"/", ""})
    public List<Item> getItems() {
        return itemInputPort.getItems();
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable String id) {
        return itemInputPort.getItem(id);
    }
}
