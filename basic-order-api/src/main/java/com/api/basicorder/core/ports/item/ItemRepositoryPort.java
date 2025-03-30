package com.api.basicorder.core.ports.item;

import com.api.basicorder.core.domain.Item;

import java.util.Optional;

public interface ItemRepositoryPort {
    void save(Item order);
    Optional<Item> findById(Long id);
}
