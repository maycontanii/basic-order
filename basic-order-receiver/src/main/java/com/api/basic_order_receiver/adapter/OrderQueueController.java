package com.api.basic_order_receiver.adapter;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OrderQueueController {

    private final SqsClient sqsClient;

    @PostConstruct
    public void listenForMessages() {
        new Thread(() -> {
            String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                            .queueName("order_processed_queue")
                            .build())
                    .queueUrl();

            while (true) {
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(5)
                        .waitTimeSeconds(10)
                        .build();

                List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

                for (Message message : messages) {
                    log.info("Order received: " + message.body());

                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build());
                }
            }
        }).start();
    }

}
