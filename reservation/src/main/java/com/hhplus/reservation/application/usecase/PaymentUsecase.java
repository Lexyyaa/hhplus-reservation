package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.PaymentInfo;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.payment.PaymentService;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.domain.reserve.Reservation;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentUsecase {
    private final WaitingQueueService queueService;
    private final UserPointService userPointService;
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final RedissonClient redissonClient;

    public PaymentResponse pay(String token, Long reservationId, Long userId){

        ReservationInfo reservation = reservationService.getReservation(reservationId);

        ConcertScheduleInfo schedule = concertService.getConcertSchedule(reservation.getConcertScheduleId());
        ConcertInfo concert = concertService.getConcert(schedule.getConcertId());

        reservationService.confirmedReservation(reservation);

        paymentService.validatePayment(reservation);
        paymentService.savePayment(userId,reservation);

        userPointService.payPoint(userId,reservation.getTotalPrice());

        queueService.updateTokenDone(token);

        return PaymentInfo.convert(userId,concert.getTitle(),reservation.getTotalPrice());
    }

    public PaymentResponse payWithDLock(String token, Long reservationId, Long userId) {

        String lockKey = "paymentLock:" + reservationId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                log.info("Payment lock start");
                try {
                    ReservationInfo reservation = reservationService.getReservation(reservationId);
                    ConcertScheduleInfo schedule = concertService.getConcertSchedule(reservation.getConcertScheduleId());
                    ConcertInfo concert = concertService.getConcert(schedule.getConcertId());

                    reservationService.confirmedReservation(reservation);
                    paymentService.validatePayment(reservation);
                    paymentService.savePayment(userId, reservation);
                    userPointService.payPoint(userId, reservation.getTotalPrice());
                    queueService.updateTokenDone(token);

                    return PaymentInfo.convert(userId, concert.getTitle(), reservation.getTotalPrice());
                } finally {
                    lock.unlock();
                    log.info("Payment unLock");
                }
            } else {
                throw new IllegalStateException("락 획득 실패");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("인터럽트 발생", e);
        }
    }
}


