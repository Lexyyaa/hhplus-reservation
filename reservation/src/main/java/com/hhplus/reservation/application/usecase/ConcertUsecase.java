package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertUsecase {
    private final ConcertService concertService;

    /**
     * 예약가능한 일정을 반환한다.
     */
    public List<ConcertScheduleResponse> getSchedules(Long concertId){
        List<ConcertScheduleInfo> schedules = concertService.getSchedules(concertId);
        return ConcertScheduleInfo.convert(schedules);
    }

    /**
     * 예약가능한 좌석목록을 반환한다.
     */
    public List<ConcertSeatResponse> getSeats(Long scheduleId){
        List<ConcertSeatInfo> seats = concertService.getSeats(scheduleId);
        return ConcertSeatInfo.convert(seats);
    }
}
