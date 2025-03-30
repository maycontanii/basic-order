package com.api.basicorder.infrastructure.dynamo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
public class OrderEntity {

    private String id;
    private Double total;
    private String itemsOrder;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @Data
    @AllArgsConstructor
    public static class ItemOrder {
        private String itemId;
        private int quantity;
        private Double total;
    }

}
