package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.*;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
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
        return jpaConcertScheduleRepository.getSchedules(concertId);
    }

    @Override
    public List<ConcertSeat> getSeats(Long concertScheduleId) {
        return jpaConcertSeatRepository.getSeats(concertScheduleId);
    }

    @Override
    public Long countSeatAvaliable(List<Long> seats) {

        List<ConcertSeat> seatList = jpaConcertSeatRepository.countSeats(seats);
        for(ConcertSeat seat : seatList) {
            System.out.println(seat.getId() + " , "+seat.getStatus());
        }

        return jpaConcertSeatRepository.countSeatAvaliable(seats);
    }

    @Override
    public void updateSeatsStatusWithLock(List<Long> seatIds, ConcertSeatStatus status) {
        try {
            List<ConcertSeat> seats = jpaConcertSeatRepository.findAllById(seatIds);
            seats.forEach(seat -> seat.setStatus(ConcertSeatStatus.UNAVAILABLE));

            jpaConcertSeatRepository.saveAll(seats);
        } catch (OptimisticLockingFailureException e) {
            throw new BizException(ErrorType.SEAT_ALREADY_RESERVED);
        }
    }

    public List<ConcertSeat> getAvailableSeatsWithLock(List<Long> seatIds) {
        return jpaConcertSeatRepository.findAvailableSeatsByIdsWithLock(seatIds);
    }

    @Override
    public int getRemainingSeats(Long concertScheduleId) {
        return jpaConcertSeatRepository.getRemainingSeats(concertScheduleId);
    }

    @Override
    public void updateScheduleStatus(Long concertScheduleId, ConcertScheduleStatus status) {
        jpaConcertScheduleRepository.updateScheduleStatus(concertScheduleId,status);
    }

    @Override
    public void updateAvailableSeats(Long concertScheduledId, int seatsSize) {
        jpaConcertScheduleRepository.updateAvailableSeats(concertScheduledId,seatsSize);
    }

    @Override
    public void restoreAvailableSeats(Long concertScheduledId, int seatsSize) {
        jpaConcertScheduleRepository.restoreAvailableSeats(concertScheduledId,seatsSize);
    }

    @Override
    public Long getTotalPrice(List<Long> seatIds) {
        return jpaConcertSeatRepository.getTotalPrice(seatIds);
    }

    @Override
    public Concert getConcert(Long concertId) {
        return jpaConcertRepository.findById(concertId).orElseThrow(
                () -> new BizException(ErrorType.CONCERT_NOT_FOUND));
    }

    @Override
    public ConcertSchedule getConcertSchedule(Long concertScheduledId) {
        return jpaConcertScheduleRepository.findById(concertScheduledId).orElseThrow(
                () -> new BizException(ErrorType.CONCERT_SCHEDULE_NOT_FOUND));
    }

    @Override
    public void updateSeatsAvaliable(List<Long> seatIds, ConcertSeatStatus concertSeatStatus) {
        jpaConcertSeatRepository.updateSeatsAvaliable(seatIds,concertSeatStatus);
    }

    @Override
    public List<ConcertSeat> getAvailableSeats(List<Long> seatIds) {
        return jpaConcertSeatRepository.findAvailableSeatsByIds(seatIds);
    }

}
