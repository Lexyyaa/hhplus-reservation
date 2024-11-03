package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.infra.point.JPAUserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserPointConcurrencyTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationConcurrencyTest.class);

    @Autowired
    private UserPointUsecase userPointUsecase;

    @Autowired
    private JPAUserPointRepository jpaUserPointRepository;

    @Test
    @DisplayName("동시 포인트 충전 시도 - 요청한 순서대로 ")
    void 동시_포인트_충전_순서대로_성공() throws InterruptedException {
        Long userId = 1L;
        Long amount = 500L;

        UserPoint currUserPoint = jpaUserPointRepository.findById(1L).orElse(null);
        Long beforePoint = currUserPoint.getPoint();

        int attemptCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(attemptCount);
        CountDownLatch latch = new CountDownLatch(attemptCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < attemptCount; i++) {
            executorService.submit(() -> {
                try {
                    userPointUsecase.chargePoint(userId, amount);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        log.info("successCount: {}", successCount.get());
        log.info("failCount: {}", failureCount.get());

        UserPoint userPoint = jpaUserPointRepository.findById(1L).orElse(null);

        assertThat(userPoint.getPoint())
                .isEqualTo(beforePoint+ (amount * attemptCount));
    }
}
