package com.test.notification.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @PostMapping("/payment-success")
    public String send(@RequestBody Map<String, String> req) {

        String orderId = req.get("orderId");

        System.out.println("Send notification for order: " + orderId);

        return "Notification sent";
    }
}