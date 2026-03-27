package com.test.payment.service;

import com.test.payment.entity.Payment;
import com.test.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    public Payment create(String orderId, Double amount) {

        log.info("Create payment request: orderId={}, amount={}", orderId, amount);

        return repo.findById(orderId).orElseGet(() -> {
            Payment p = new Payment();
            p.setOrderId(orderId);
            p.setAmount(amount);
            p.setStatus(Payment.Status.PENDING);

            log.info("New payment created: orderId={}", orderId);

            return repo.save(p);
        });
    }

    public String callback(String orderId, Payment.Status status) {

        log.info("Callback received: orderId={}, status={}", orderId, status);

        Payment payment = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        //  IDEMPOTENCY CHECK
        if (payment.getStatus() == Payment.Status.SUCCESS) {
            log.warn("Duplicate callback ignored for orderId={}", orderId);
            return "Already processed";
        }

        payment.setStatus(status);
        repo.save(payment);

        log.info("Payment updated: orderId={} -> {}", orderId, status);

        // SIMULASI EVENT (buat jawab soal Kafka)
        log.info("Emit event: payment success for orderId={}", orderId);

        return "Updated";
    }
}