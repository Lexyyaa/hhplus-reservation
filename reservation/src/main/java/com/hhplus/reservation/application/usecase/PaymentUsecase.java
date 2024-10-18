package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.PaymentInfo;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.payment.PaymentService;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentUsecase {
    private final WaitingQueueService queueService;
    private final UserPointService userPointService;
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final PaymentService paymentService;

    public PaymentResponse pay(String token, Long reservationId, Long userId, Long amount){

        queueService.validateToken(token);

        ReservationInfo reservation = reservationService.getReservation(reservationId);
        ConcertScheduleInfo schedule = concertService.getConcertSchedule(reservation.getConcertScheduleId());
        ConcertInfo concert = concertService.getConcert(schedule.getConcertId());

        paymentService.validatePayment(reservation);

        userPointService.payPoint(userId,reservation.getTotalPrice());

        paymentService.savePayment(userId,reservation);

        reservationService.confirmedReservation(reservation);

        queueService.updateTokenDone(token);

        return PaymentInfo.convert(userId,concert.getTitle(),reservation.getTotalPrice());
    }
}


