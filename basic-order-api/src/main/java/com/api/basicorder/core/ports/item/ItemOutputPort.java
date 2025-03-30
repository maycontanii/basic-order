package com.api.basicorder.core.ports.item;

import com.api.basicorder.core.domain.Item;
import com.api.basicorder.core.domain.Order;

import java.util.List;
import java.util.Optional;

public interface ItemOutputPort {

    Item save(Item item);

    Optional<Item> findById(String id);

    List<Item> findAll();
}
