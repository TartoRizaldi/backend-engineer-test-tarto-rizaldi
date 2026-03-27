package com.test.payment.service;

import com.test.payment.entity.Payment;
import com.test.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    public Payment create(String orderId, Double amount) {
        return repo.findById(orderId).orElseGet(() -> {
            Payment p = new Payment();
            p.setOrderId(orderId);
            p.setAmount(amount);
            p.setStatus(Payment.Status.PENDING);
            return repo.save(p);
        });
    }

    public String callback(String orderId, Payment.Status status) {
        Payment payment = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // IDEMPOTENT CHECK
        if (payment.getStatus() == Payment.Status.SUCCESS) {
            return "Already processed";
        }

        payment.setStatus(status);
        repo.save(payment);

        return "Updated";
    }
}