package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.domain.reserve.ReservationType;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfo {
    Long id;
    Long userId;
    Long concertScheduleId;
    Long totalPrice;
    ReservationType reserved;
    LocalDateTime reserveRequestAt;
    LocalDateTime reserveExpiredAt;

    public static ReserveResponse convert(ReservationInfo reservation) {
        return ReserveResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .concertScheduleId(reservation.getConcertScheduleId())
                .totalPrice(reservation.getTotalPrice())
                .reserved(reservation.getReserved())
                .reserveRequestAt(reservation.getReserveRequestAt())
                .reserveExpiredAt(reservation.getReserveExpiredAt())
                .build();
    }
}