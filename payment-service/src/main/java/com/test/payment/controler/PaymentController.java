package com.test.payment.controller;

import com.test.payment.entity.Payment;
import com.test.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Payment create(@RequestBody Map<String, Object> req) {
        return service.create(
                (String) req.get("orderId"),
                Double.valueOf(req.get("amount").toString())
        );
    }

    @PostMapping("/callback")
    public String callback(@RequestBody Map<String, String> req) {
        return service.callback(
                req.get("orderId"),
                Payment.Status.valueOf(req.get("status"))
        );
    }
}