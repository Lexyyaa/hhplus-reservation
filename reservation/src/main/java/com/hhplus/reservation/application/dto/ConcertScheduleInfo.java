package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConcertScheduleInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "ConcertScheduleInfo{" +
                "id=" + id +
                ", concertId=" + concertId +
                ", performDate=" + performDate +
                ", totalSeat=" + totalSeat +
                ", availableSeatNum=" + availableSeatNum +
                ", availableStatus=" + availableStatus +
                '}';
    }
}
