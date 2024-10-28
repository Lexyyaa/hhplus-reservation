package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PaymentConcurrencyTest {

    @Autowired
    private PaymentUsecase paymentUsecase;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final CountDownLatch latch = new CountDownLatch(3);

    @Test
    @DisplayName("동시 결제 시도 - 한 명만 성공")
    @Transactional
    void 동시_결제_시도_한명만_성공() throws InterruptedException, ExecutionException {
        String token = "valid_token";
        Long reservationId = 1L;
        Long userId = 1L;
        
        List<Future<Boolean>> results = executorService.invokeAll(List.of(
                () -> attemptPayment(token, reservationId, userId),
                () -> attemptPayment(token, reservationId, userId),
                () -> attemptPayment(token, reservationId, userId)
        ));

        int successCount = 0;
        int alreadyPaidCount = 0;

        for (Future<Boolean> result : results) {
            Boolean paymentResult = result.get();
            if (paymentResult) {
                successCount++;
            } else {
                alreadyPaidCount++;
            }
        }

        assertThat(successCount).isEqualTo(1); // 성콩카운트 1
        assertThat(alreadyPaidCount).isEqualTo(2); // 실패카운트 2
    }

    Boolean attemptPayment(String token, Long reservationId, Long userId) {
        try {
            latch.countDown();
            latch.await();

            paymentUsecase.pay(token, reservationId, userId);

            return true;
        } catch (BizException e) {
            if (e.getErrorType() == ErrorType.PAYMENT_ALREADY_MADE) {
                System.out.println("이미 결제된 예약");
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
