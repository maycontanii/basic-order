package com.api.basicorder.core.ports.order;

import com.api.basicorder.core.domain.Order;

import java.util.Optional;

public interface OrderRepositoryPort {
    void save(Order order);
    Optional<Order> findById(Long id);
}
