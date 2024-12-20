package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationUsecase {

    private final ConcertService concertService;
    private final ReservationService reservationService;

    /**
     * 선택한 죄석을 예약한다.(임시예약)
     */
    public ReserveResponse reserve(Long concertScheduleId, ReserveSeatRequest request){
        List<Long> seats = request.getSeats();
        Long userId = request.getUserId();
        Long totalPrice = concertService.updateSeatStatus(concertScheduleId,seats);
        ReservationInfo reservation = reservationService.reserve(concertScheduleId, userId, totalPrice, seats);
        return ReservationInfo.convert(reservation);
    }
}
