package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.infra.reservation.JPAReservationRepository;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ReservationConcurrencyTest {

    @Autowired
    private ReservationUsecase reservationUsecase;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final CountDownLatch latch = new CountDownLatch(3);

    @Test
    @DisplayName("동시 예약 시도 - 한 명만 성공")
    @Transactional
    void 동시_예약_시도_한명만_성공() throws InterruptedException, ExecutionException {
        Long concertScheduleId = 1L;
        Long seatId = 5L;

        List<Future<Boolean>> results = executorService.invokeAll(List.of(
                () -> attemptReservation(1L, concertScheduleId, seatId),
                () -> attemptReservation(2L, concertScheduleId, seatId),
                () -> attemptReservation(3L, concertScheduleId, seatId)
        ));

        int successCount = 0;
        int alreadyReservedCount = 0;

        for (Future<Boolean> result : results) {
            Boolean reservationResult = result.get();
            if (reservationResult) {
                successCount++;
            } else {
                alreadyReservedCount++;
            }
        }

        assertThat(successCount).isEqualTo(1); // 성공 1건
        assertThat(alreadyReservedCount).isEqualTo(2); // 실패 2건 
    }

    Boolean attemptReservation(Long userId, Long concertScheduleId, Long seatId) {
        try {
            latch.countDown();
            latch.await();

            reservationUsecase.reserve(
                    concertScheduleId,
                    new ReserveSeatRequest(userId, List.of(seatId))
            );
            System.out.println("예약 성공");
            return true;
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("좌석 이미 예약됨");
            return false;
        } catch (BizException e) {
            if (e.getErrorType() == ErrorType.SEAT_ALREADY_RESERVED) {
                System.out.println("예약 실패");
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
