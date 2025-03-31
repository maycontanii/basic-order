package com.api.basicorder.infrastructure.dynamo;

import com.api.basicorder.core.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class DynamoConfig {

    private final String ENDPOINT = "http://localstack:4566";
    //    private final String ENDPOINT = "http://localhost:4566"; -- rodar local
    private final String REGION = "us-east-1";

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        log.info("Configuring DynamoDbClient");

        try {
            DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                    .endpointOverride(URI.create(ENDPOINT))
                    .region(Region.of(REGION))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("test", "test")
                    ))
                    .build();

            if (dynamoDbClient.listTables().tableNames().isEmpty()) {
                log.info("Running migrations...");

                createTableOrder(dynamoDbClient);
                createTableItem(dynamoDbClient);
                insertItems(dynamoDbClient);

                log.info("Finished migrations");
            }

            return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        } catch (Exception e) {
            log.error("Connection failed to DynamoDbClient");
            throw new RuntimeException(e);
        }
    }

    private void createTableOrder(DynamoDbClient dynamoDbClient) {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName("Order")
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build()
                )
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build()
                )
                .build();

        createTable(dynamoDbClient, request);
    }

    private void createTableItem(DynamoDbClient dynamoDbClient) {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName("Item")
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build()
                )
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build()
                )
                .build();

        createTable(dynamoDbClient, request);
    }

    private void insertItems(DynamoDbClient dynamoDbClient) {
        List<Item> items = new ArrayList<>();

        items.add(new Item("uuid-pen", "Pen", 1.5));
        items.add(new Item("uuid-pencil", "Pencil", 1.0));
        items.add(new Item("uuid-eraser", "Eraser", 0.5));

        for (Item item : items) {
            Map<String, AttributeValue> itemMap = new HashMap<>();

            itemMap.put("id", AttributeValue.builder().s(item.getId()).build());
            itemMap.put("title", AttributeValue.builder().s(item.getTitle()).build());
            itemMap.put("unitValue", AttributeValue.builder().n(item.getUnitValue().toString()).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("Item")
                    .item(itemMap)
                    .build();

            dynamoDbClient.putItem(putItemRequest);
        }

        log.info("Insert items");
    }

    private static void createTable(DynamoDbClient dynamoDbClient, CreateTableRequest request) {
        try {
            dynamoDbClient.createTable(request);
            log.info("Create table " + request.tableName());
        } catch (Exception e) {
            log.error("Create table item failed " + request.tableName());
            throw new RuntimeException(e);
        }
    }

}
