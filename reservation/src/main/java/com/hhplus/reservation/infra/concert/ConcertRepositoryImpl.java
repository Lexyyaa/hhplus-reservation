package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.*;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final JPAConcertRepository jpaConcertRepository;
    private final JPAConcertScheduleRepository jpaConcertScheduleRepository;
    private final JPAConcertSeatRepository jpaConcertSeatRepository;

    @Override
    public List<ConcertSchedule> getSchedules(Long concertId) {
        List<ConcertSchedule> schedules = jpaConcertScheduleRepository.getSchedules(concertId);
        if(schedules.isEmpty()){
            throw new CustomException(ErrorCode.CONCERT_SCHEDULE_NOT_FOUND);
        }
        return schedules;
    }

    @Override
    public List<ConcertSeat> getSeats(Long concertScheduleId) {
        List<ConcertSeat> seats = jpaConcertSeatRepository.getSeats(concertScheduleId);
        if(seats.size() < 1){
            throw new CustomException(ErrorCode.SEATS_NOT_FOUND);
        }
        return seats;
    }

    @Override
    public Long countSeatAvaliable(List<Long> seats) {
        List<Long> seatIds = List.of();
        return jpaConcertSeatRepository.countSeatAvaliable(seats);
    }

    @Override
    public void updateSeatsStatusWithLock(List<Long> seatIds, ConcertSeatStatus status) {

        try {
            List<ConcertSeat> seats = jpaConcertSeatRepository.findAllById(seatIds);
            seats.forEach(seat -> seat.setStatus(ConcertSeatStatus.UNAVALIABLE));

            jpaConcertSeatRepository.saveAll(seats);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("이미 예약된 좌석입니다.");
        }
    }

    @Override
    public void updateAvailableSeats(Long concertScheduledId, int seatsSize) {
        jpaConcertScheduleRepository.updateAvailableSeats(concertScheduledId,seatsSize);
    }

    @Override
    public Long getTotalPrice(List<Long> seatIds) {
        return jpaConcertSeatRepository.getTotalPrice(seatIds);
    }

    @Override
    public Concert getConcert(Long concertId) {
        return jpaConcertRepository.findById(concertId).orElseThrow(
                () -> new CustomException(ErrorCode.CONCERT_NOT_FOUND));
    }

    @Override
    public ConcertSchedule getConcertSchedule(Long concertScheduledId) {
        return jpaConcertScheduleRepository.findById(concertScheduledId).orElseThrow(
                () -> new CustomException(ErrorCode.CONCERT_SCHEDULE_NOT_FOUND));
    }

    @Override
    public void updateSeatsAvaliable(List<Long> seatIds, ConcertSeatStatus concertSeatStatus) {
        jpaConcertSeatRepository.updateSeatsAvaliable(seatIds,concertSeatStatus);
    }

}
