package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertCacheRepository concertCacheRepository;
    private final ConcertRepository concertRepository;

    /**
     * 선택한 콘서트의 예약 가능한 일정 목록을 반환한다.
     */
    public List<ConcertScheduleInfo> getSchedules(Long concertId) {
        List<ConcertScheduleInfo> cachedSchedules = concertCacheRepository.getCachedSchedules(concertId);

        if (cachedSchedules != null && !cachedSchedules.isEmpty()) {
            return cachedSchedules;
        }

        List<ConcertSchedule> schedules = concertRepository.getSchedules(concertId);
        ConcertSchedule.isEmptyScheduleList(schedules);
        List<ConcertScheduleInfo> scheduleInfoList = ConcertSchedule.convert(schedules);

        concertCacheRepository.cacheSchedules(concertId, scheduleInfoList);

        return scheduleInfoList;
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

        List<ConcertSeat> avaliableSeats = concertRepository.getAvailableSeats(seats);
        ConcertSeat.chkAllSeatAvaliable(seats.size(),avaliableSeats.size());

        concertRepository.updateSeatsStatusWithLock(seats, ConcertSeatStatus.UNAVAILABLE);
        concertRepository.updateAvailableSeats(concertScheduleId, seats.size());

        Long totalPrice = concertRepository.getTotalPrice(seats);

        int remainingSeats = concertRepository.getRemainingSeats(concertScheduleId);
        if (remainingSeats == 0) {
            concertRepository.updateScheduleStatus(concertScheduleId, ConcertScheduleStatus.UNAVAILABLE); // 예약 불가 상태로 업데이트
        }

        concertCacheRepository.evictSchedulesCache(concertScheduleId);

        return totalPrice;
    }
}
