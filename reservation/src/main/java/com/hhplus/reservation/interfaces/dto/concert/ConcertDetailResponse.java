package com.hhplus.reservation.interfaces.dto.concert;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertDetailResponse {
    Long id;
    Long concertId;
    LocalDate performDate;
    Long totalSeat;
    Long availableSeat;
    ConcertSeatStatus availableStatus;
}
