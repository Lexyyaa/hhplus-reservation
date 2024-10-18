package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationUsecase {

    private final WaitingQueueService queueService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    public ReserveResponse reserve(Long concertScheduleId, Long userId, String token, ReserveSeatRequest request){
        queueService.validateToken(token);

        List<Long> seats = request.getSeats();
        Long totalPrice = concertService.updateSeatStatus(concertScheduleId,seats);
        ReservationInfo reservation = reservationService.reserve(concertScheduleId, userId, totalPrice, seats);
        return ReservationInfo.convert(reservation);
    }
}
