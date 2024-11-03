package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.domain.point.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentConcurrencyWithDLockTest {

    @Autowired
    private PaymentUsecase paymentUsecase;

    @Autowired
    private UserPointRepository userPointRepository;

    @Test
    @DisplayName("동시 결제 시도 - 락 사용, 예약 건 만큼만 포인트 차감")
    void 동시_결제_시도_정상적인_포인트_차감_락_사용() throws InterruptedException {
        String token = "valid_token";
        Long reservationId = 1L;
        Long totalPrice = 40000L;
        Long userId = 1L;
        Long initialPoint = 40000L;

        int attemptCount = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(attemptCount);
        CountDownLatch latch = new CountDownLatch(attemptCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < attemptCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentUsecase.payWithDLock(token, reservationId, userId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        UserPoint userPoint = userPointRepository.findByUserId(userId);

        // 포인트가 정확히 차감되었는지 확인
        assertThat(userPoint.getPoint()).isEqualTo(initialPoint - totalPrice);

        // 오직 하나의 결제 시도만 성공했는지 확인
        assertThat(successCount.get()).isEqualTo(1);

        // 나머지 시도는 실패했는지 확인
        assertThat(failCount.get()).isEqualTo(attemptCount - 1);
    }
}
