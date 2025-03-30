package com.api.basicorder.core.adapters.out;

import com.api.basicorder.core.domain.Order;
import com.api.basicorder.core.ports.order.OrderOutputPort;
import com.api.basicorder.infrastructure.dynamo.entity.OrderEntity;
import com.api.basicorder.infrastructure.dynamo.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DynamoOrderRepository implements OrderOutputPort {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private DynamoDbTable<OrderEntity> getDynamoDbTable() {
        return dynamoDbEnhancedClient.table("Order", TableSchema.fromBean(OrderEntity.class));
    }

    @Override
    public String save(OrderEntity order) {
        getDynamoDbTable().putItem(order);
        return order.getId();
    }

    @Override
    public Optional<Order> findById(String id) throws JsonProcessingException {
        OrderEntity orderEntity = getDynamoDbTable().getItem(r -> r.key(k -> k.partitionValue(id)));

        if (Objects.isNull(orderEntity)) return Optional.empty();

        return Optional.of(OrderMapper.toDomain(orderEntity));
    }
}
