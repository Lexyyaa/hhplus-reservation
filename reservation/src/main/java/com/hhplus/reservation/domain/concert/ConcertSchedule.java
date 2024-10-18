package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "concert_schedule")
public class ConcertSchedule extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "perform_date")
    private LocalDate performDate;

    @Column(name = "total_seat")
    private Long totalSeat;

    @Column(name = "available_seat_num")
    private Long availableSeatNum;

    @Column(name = "available_status")
    @Enumerated(EnumType.STRING)
    private ConcertSeatStatus availableStatus;

    public static List<ConcertScheduleInfo> convert(List<ConcertSchedule> schedules){
        return schedules.stream()
                .map(schedule -> ConcertScheduleInfo.builder()
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


    public static ConcertScheduleInfo convert(ConcertSchedule schedule){
        return ConcertScheduleInfo.builder()
                        .id(schedule.getId())
                        .concertId(schedule.getConcertId())
                        .performDate(schedule.getPerformDate())
                        .totalSeat(schedule.getTotalSeat())
                        .availableSeatNum(schedule.getAvailableSeatNum())
                        .availableStatus(schedule.getAvailableStatus())
                        .build();
    }
}