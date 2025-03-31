package com.api.basicorder.core.adapters.out;

import com.api.basicorder.core.domain.Item;
import com.api.basicorder.core.ports.item.ItemOutputPort;
import com.api.basicorder.infrastructure.dynamo.entity.ItemEntity;
import com.api.basicorder.infrastructure.dynamo.mapper.ItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ItemRepository implements ItemOutputPort {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private DynamoDbTable<ItemEntity> getDynamoDbTable() {
        return dynamoDbEnhancedClient.table("Item", TableSchema.fromBean(ItemEntity.class));
    }

    @Override
    public Item save(Item item) {
        return new Item();
    }

    @Override
    public Optional<Item> findById(String id) {
        ItemEntity itemEntity = getDynamoDbTable().getItem(r -> r.key(k -> k.partitionValue(id)));

        if (Objects.isNull(itemEntity)) return Optional.empty();

        return Optional.of(ItemMapper.toDomain(itemEntity));
    }

    @Override
    public List<Item> findAll() {
        return getDynamoDbTable().scan().items().stream()
                .map(ItemMapper::toDomain)
                .collect(Collectors.toList());
    }

}
