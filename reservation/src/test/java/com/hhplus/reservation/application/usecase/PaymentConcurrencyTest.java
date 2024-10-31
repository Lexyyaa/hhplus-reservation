package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.domain.reserve.Reservation;
import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.infra.point.UserPointRepositoryImpl;
import com.hhplus.reservation.infra.reservation.JPAReservationRepository;
import com.hhplus.reservation.infra.reservation.ReservationRepositoryImpl;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PaymentConcurrencyTest {

    @Autowired
    private PaymentUsecase paymentUsecase;

    @Autowired
    private UserPointRepository  userPointRepository;

    @Test
    @DisplayName("동시 결제 시도 - 예약 건 만큼만 포인트 차감")
    void 동시_결제_시도_정상적인_포인트_차감() throws InterruptedException {
        String token = "valid_token";
        Long reservationId = 1L;
        Long totalPrice = 40000L;
        Long userId = 1L;
        Long point = 40000L;

        int attemptCount = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(attemptCount);
        CountDownLatch latch = new CountDownLatch(attemptCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < attemptCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentUsecase.pay(token, reservationId, userId);
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

        assertThat(userPoint.getPoint()).isEqualTo(point-totalPrice);

        assertThat(successCount.get()).isEqualTo(1);

        assertThat(failCount.get()).isEqualTo(attemptCount - 1);
    }
}

