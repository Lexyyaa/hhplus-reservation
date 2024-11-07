package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import jakarta.persistence.*;
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
@Entity
@Table(name = "concert_schedule")
public class ConcertSchedule extends Timestamped implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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


    public static void isEmptyScheduleList(List<ConcertSchedule> schedules){
        if(schedules.isEmpty()){
            throw new BizException(ErrorType.CONCERT_SCHEDULE_NOT_FOUND);
        }
    }
}