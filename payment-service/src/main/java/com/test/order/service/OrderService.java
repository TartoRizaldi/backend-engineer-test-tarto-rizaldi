package com.test.order.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public String createOrder(String orderId, Double amount) {

        log.info("Create order: orderId={}, amount={}", orderId, amount);

        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        request.put("amount", amount);

        int retries = 3;

        for (int i = 0; i < retries; i++) {
            try {
                log.info("Calling payment service... attempt={}", i + 1);

                restTemplate.postForObject(
                        "http://localhost:8081/payment/create",
                        request,
                        String.class
                );

                return "Order Created & Payment Initiated";

            } catch (Exception e) {
                log.error("Error calling payment service: {}", e.getMessage());

                try {
                    Thread.sleep((long) Math.pow(2, i) * 100);
                } catch (InterruptedException ignored) {}
            }
        }

        return "Failed after retries";
    }}