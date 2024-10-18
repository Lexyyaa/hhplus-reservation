package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertUsecase {
    private final WaitingQueueService queueService;
    private final ConcertService concertService;
    public List<ConcertScheduleResponse> getSchedules(Long concertId, String token){
        queueService.validateToken(token);
        List<ConcertScheduleInfo> schedules = concertService.getSchedules(concertId);
        return ConcertScheduleInfo.convert(schedules);
    }

    public List<ConcertSeatResponse> getSeats(Long scheduleId, String token){
        queueService.validateToken(token);
        List<ConcertSeatInfo> seats = concertService.getSeats(scheduleId);
        return ConcertSeatInfo.convert(seats);
    }
}
