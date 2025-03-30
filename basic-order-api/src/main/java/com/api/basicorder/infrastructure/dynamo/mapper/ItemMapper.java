package com.api.basicorder.infrastructure.dynamo.mapper;

import com.api.basicorder.core.domain.Item;
import com.api.basicorder.infrastructure.dynamo.entity.ItemEntity;

public class ItemMapper {

    public static ItemEntity toEntity(Item item) {
        ItemEntity entity = new ItemEntity();
        entity.setId(item.getId());
        entity.setTitle(item.getTitle());
        entity.setUnitValue(item.getUnitValue());
        return entity;
    }

    public static Item toDomain(ItemEntity entity) {
        return new Item(entity.getId(), entity.getTitle(), entity.getUnitValue());
    }
}
