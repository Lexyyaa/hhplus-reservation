package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertScheduleInfo {
    Long id;
    Long concertId;
    LocalDate performDate;
    Long totalSeat;
    Long availableSeatNum;
    ConcertSeatStatus availableStatus;

    public static List<ConcertScheduleResponse> convert(List<ConcertScheduleInfo> schedules){
        return schedules.stream()
                .map(schedule -> ConcertScheduleResponse.builder()
                        .id(schedule.getId())
                        .concertId(schedule.getConcertId())
                        .performDate(schedule.getPerformDate())
                        .totalSeat(schedule.getTotalSeat())
                        .availableSeatNum(schedule.getAvailableSeatNum())
                        .availableStatus(schedule.getAvailableStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
