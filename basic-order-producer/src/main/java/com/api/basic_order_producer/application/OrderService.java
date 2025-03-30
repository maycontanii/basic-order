package com.api.basic_order_producer.application;

import com.api.basic_order_producer.core.domain.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public static String TOPIC_NAME = "order_topic";
    private static List<String> activesItems = List.of("uuid-pen", "uuid-pencil", "uuid-eraser");

    @Scheduled(fixedRate = 1000)
    private void execute() {
        try {
            List<Order.Item> items = new ArrayList<>();
            items.add(new Order.Item(activesItems.get(random.nextInt(activesItems.size())), random.nextInt(100) + 1));

            Order order = new Order(items);

            String message = objectMapper.writeValueAsString(order);

            String topicArn = getTopicArnByName(TOPIC_NAME);

            if (Objects.isNull(topicArn)) throw new RuntimeException("Topic SNS not found " + TOPIC_NAME);

            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();

            PublishResponse response = snsClient.publish(publishRequest);
            log.info("Order published: " + response.messageId());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTopicArnByName(String topicName) {
        ListTopicsResponse listTopicsResponse = snsClient.listTopics();
        List<Topic> topics = listTopicsResponse.topics();
        return topics.stream()
                .map(Topic::topicArn)
                .filter(arn -> arn.endsWith(":" + topicName))
                .findFirst()
                .orElse(null);
    }

}
