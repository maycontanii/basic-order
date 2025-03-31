package com.api.basicorder.core.ports.order;

import com.api.basicorder.core.domain.Order;
import com.api.basicorder.infrastructure.dynamo.entity.OrderEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface OrderSqsOutputPort {

    void postOnSqsQueue(String queueName, String order);
}
