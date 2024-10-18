package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.domain.concert.ConcertSeatType;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatInfo {
    long id;
    long concertScheduleId;
    String seatNum;
    ConcertSeatType seatType;
    long seatPrice;
    ConcertSeatStatus status;

    public static List<ConcertSeatResponse> convert(List<ConcertSeatInfo> seats){
        return seats.stream()
                .map(seat -> ConcertSeatResponse.builder()
                        .id(seat.getId())
                        .concertScheduleId(seat.getConcertScheduleId())
                        .seatNum(seat.getSeatNum())
                        .seatPrice(seat.getSeatPrice())
                        .status(seat.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
