package com.api.basicorder.core.adapters.in;

import com.api.basicorder.core.adapters.in.dto.SaveOrderInput;
import com.api.basicorder.core.ports.order.OrderInputPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final OrderInputPort orderInputPort;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void listenForMessages() {
        new Thread(() -> {
            String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                            .queueName("order_process_queue")
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
                    try {
                        String jsonOrder = objectMapper.readTree(message.body()).get("Message").asText();
                        SaveOrderInput saveOrderInput = objectMapper.readValue(jsonOrder, SaveOrderInput.class);
                        String orderProcessId = orderInputPort.saveOrder(saveOrderInput);
                        log.info("Order processed: " + orderProcessId);

                        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .receiptHandle(message.receiptHandle())
                                .build());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
