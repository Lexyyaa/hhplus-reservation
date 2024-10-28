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

    /**
     * 선택한 콘서트의 예약가능한 일정목록을 반환한다.
     */
    public List<ConcertScheduleInfo> getSchedules(Long concertId){
        List<ConcertSchedule> schedules = concertRepository.getSchedules(concertId);
        ConcertSchedule.isEmptyScheduleList(schedules);
        return ConcertSchedule.convert(schedules);
    }

    /**
     * 선택한 일정에서 예약가능한 좌석목록을 반환한다.
     */
    public List<ConcertSeatInfo> getSeats(Long scheduleId){
        List<ConcertSeat> seats = concertRepository.getSeats(scheduleId);
        ConcertSeat.isEmptySeatList(seats);
        return ConcertSeat.convert(seats);
    }

    /**
     * 콘서트 정보를 반환한다.
     */
    public ConcertInfo getConcert(Long concertId){
        Concert currConcert = concertRepository.getConcert(concertId);
        return Concert.convert(currConcert);
    }

    /**
     * 선택한 콘서트의 일정정보를 반환한다.
     */
    public ConcertScheduleInfo getConcertSchedule(Long concertScheduleId){
        ConcertSchedule currConcertSchedule = concertRepository.getConcertSchedule(concertScheduleId);
        return ConcertSchedule.convert(currConcertSchedule);
    }

    /**
     * 예약시 해당 좌석을 예약불가 상태로 업데이트하며, 예약좌석에 대한 합산금액을 계산한다.
     */
    @Transactional
    public Long updateSeatStatus(Long concertScheduleId,List<Long> seats){
        Long avaliableCnt = concertRepository.countSeatAvaliable(seats);
        ConcertSeat.chkAllSeatAvaliable(seats.size(),avaliableCnt);

        concertRepository.updateSeatsStatusWithLock(seats, ConcertSeatStatus.UNAVAILABLE);
        concertRepository.updateAvailableSeats(concertScheduleId, seats.size());

        Long totalPrice = concertRepository.getTotalPrice(seats);
        return totalPrice;
    }

}
