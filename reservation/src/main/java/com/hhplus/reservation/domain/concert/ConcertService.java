package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public List<ConcertScheduleInfo> getSchedules(Long concertId){
        List<ConcertSchedule> schedules = concertRepository.getSchedules(concertId);
        return ConcertSchedule.convert(schedules);
    }

    public List<ConcertSeatInfo> getSeats(Long scheduleId){
        List<ConcertSeat> seats = concertRepository.getSeats(scheduleId);
        return ConcertSeat.convert(seats);
    }

    public ConcertInfo getConcert(Long concertId){
        Concert currConcert = concertRepository.getConcert(concertId);
        return Concert.convert(currConcert);
    }

    public ConcertScheduleInfo getConcertSchedule(Long concertScheduleId){
        ConcertSchedule currConcertSchedule = concertRepository.getConcertSchedule(concertScheduleId);
        return ConcertSchedule.convert(currConcertSchedule);
    }

    @Transactional
    public Long updateSeatStatus(Long concertScheduleId,List<Long> seats){
        Long avaliableCnt = concertRepository.countSeatAvaliable(seats);
        ConcertSeat.chkAllSeatAvaliable(seats.size(),avaliableCnt);

        concertRepository.updateSeatsStatusWithLock(seats, ConcertSeatStatus.UNAVALIABLE);
        concertRepository.updateAvailableSeats(concertScheduleId, seats.size());

        Long totalPrice = concertRepository.getTotalPrice(seats);
        return totalPrice;
    }
}
