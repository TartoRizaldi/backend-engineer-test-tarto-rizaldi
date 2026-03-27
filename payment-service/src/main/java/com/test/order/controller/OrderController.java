package com.test.order.controller;

import com.test.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public String create(@RequestBody Map<String, Object> req) {

        String orderId = (String) req.get("orderId");
        Double amount = Double.valueOf(req.get("amount").toString());

        if (orderId == null || amount == null) {
            throw new RuntimeException("Invalid request");
        }

        return service.createOrder(orderId, amount);
    }
}