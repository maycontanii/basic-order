package com.api.basicorder.core.adapters.in;

import com.api.basicorder.core.adapters.in.dto.SaveOrderInput;
import com.api.basicorder.core.domain.Order;
import com.api.basicorder.core.ports.order.OrderInputPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderInputPort orderInputPort;

    @PostMapping({"/", ""})
    public String saveOrder(@RequestBody SaveOrderInput input) {
        return orderInputPort.saveOrder(input);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderInputPort.getOrder(id);
    }
}
