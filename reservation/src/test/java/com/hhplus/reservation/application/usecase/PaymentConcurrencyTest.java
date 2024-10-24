package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.payment.PaymentRepository;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentConcurrencyTest {

    @Autowired
    private PaymentUsecase paymentUsecase;

    @Autowired
    private PaymentRepository paymentRepository;

    // 결제 동시성 테스트
}
