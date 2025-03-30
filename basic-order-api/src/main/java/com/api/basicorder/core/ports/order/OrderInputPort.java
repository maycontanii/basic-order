package com.api.basicorder.core.ports.order;

import com.api.basicorder.core.adapters.in.dto.SaveOrderInput;
import com.api.basicorder.core.domain.Order;

public interface OrderInputPort {
    Order getOrder(String id);

    String saveOrder(SaveOrderInput input);
}
