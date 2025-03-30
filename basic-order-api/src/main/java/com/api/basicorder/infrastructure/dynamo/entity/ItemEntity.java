package com.api.basicorder.infrastructure.dynamo.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
public class ItemEntity {

    private String id;
    private String title;
    private Double unitValue;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

}
