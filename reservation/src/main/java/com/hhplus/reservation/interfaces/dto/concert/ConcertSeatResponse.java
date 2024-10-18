package com.hhplus.reservation.interfaces.dto.concert;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.domain.concert.ConcertSeatType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatResponse {
    long id;
    long concertScheduleId;
    String seatNum;
    ConcertSeatType seatType;
    long seatPrice;
    ConcertSeatStatus status;
}

