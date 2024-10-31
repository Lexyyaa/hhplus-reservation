package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.concert.Concert;
import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.infra.reservation.JPAReservationRepository;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationConcurrencyTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationConcurrencyTest.class);

    @Autowired
    private ReservationUsecase reservationUsecase;

    private static final int NUMBER_OF_USERS = 1000;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_USERS);
    private CountDownLatch latch = new CountDownLatch(NUMBER_OF_USERS);

    @Test
    @DisplayName("동시 예약 시도 - 1000명 중 한 명만 성공")
    void 동시_예약_시도_1000명_한명만_성공() throws InterruptedException {
        Long concertScheduleId = 1L;
        Long seatId = 5L;

        List<Callable<Boolean>> tasks = IntStream.rangeClosed(1, NUMBER_OF_USERS)
                .mapToObj(userId -> (Callable<Boolean>) () -> attemptReservation((long) userId, concertScheduleId, seatId))
                .toList();

        List<Future<Boolean>> results = executorService.invokeAll(tasks);

        long successCount = 0;
        long failureCount = 0;

        for (Future<Boolean> result : results) {
            try {
                if (result.get()) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                log.error("결과 가져오기 실패: {}", e.getMessage());
            }
        }

        assertThat(successCount).isEqualTo(1);
        assertThat(failureCount).isEqualTo(NUMBER_OF_USERS - 1);
    }

    Boolean attemptReservation(Long userId, Long concertScheduleId, Long seatId) {
        try {
            latch.countDown();
            latch.await();

            reservationUsecase.reserve(
                    concertScheduleId,
                    new ReserveSeatRequest(userId, List.of(seatId))
            );
            log.info("예약 성공: userId={}, concertScheduleId={} , seatIds={}", userId ,concertScheduleId ,List.of(seatId));
            return true;
        } catch (BizException e) {
            log.warn("예약 실패: userId={}, 원인={}", userId, e.getErrorType().getMessage());
            return false;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("스레드 인터럽트 발생: userId={}", userId);
            return false;
        }
    }
}
