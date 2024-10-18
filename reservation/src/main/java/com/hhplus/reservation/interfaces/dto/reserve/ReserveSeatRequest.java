package com.hhplus.reservation.interfaces.dto.reserve;

import com.hhplus.reservation.domain.concert.ConcertSeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveSeatRequest {

    private Long userId;
    private List<ReserveSeat> seatList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReserveSeat {
        private Long concertDetailId;
        private String seatNum;
        private ConcertSeatType type;
        private Long price;
    }
}
