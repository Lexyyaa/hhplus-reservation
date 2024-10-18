package com.hhplus.reservation.interfaces.dto.concert;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertScheduleResponse {
    Long id;
    Long concertId;
    LocalDate performDate;
    Long totalSeat;
    Long availableSeatNum;
    ConcertSeatStatus availableStatus;
}