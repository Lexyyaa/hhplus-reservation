package com.hhplus.reservation.interfaces.dto.concert;

import com.hhplus.reservation.domain.concert.ConcertSeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatResponse {
    long id;
    long concertDetailId;
    String seatNum;
    ConcertSeatType seatType;
    long seatPrice;
}
