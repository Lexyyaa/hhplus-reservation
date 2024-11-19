package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.PaymentInfo;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.payment.PaymentDeleteTokenCommand;
import com.hhplus.reservation.domain.payment.PaymentEventPublisher;
import com.hhplus.reservation.domain.payment.PaymentService;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentUsecase {
    private final UserPointService userPointService;
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentResponse pay(String token, Long reservationId, Long userId){

        ReservationInfo reservation = reservationService.getReservation(reservationId);

        ConcertScheduleInfo schedule = concertService.getConcertSchedule(reservation.getConcertScheduleId());
        ConcertInfo concert = concertService.getConcert(schedule.getConcertId());

        reservationService.confirmedReservation(reservation);

        paymentService.validatePayment(reservation);
        paymentService.savePayment(userId,reservation);

        userPointService.payPoint(userId,reservation.getTotalPrice());

        paymentEventPublisher.deleteToken(new PaymentDeleteTokenCommand(token));

        return PaymentInfo.convert(userId,concert.getTitle(),reservation.getTotalPrice());
    }
}


