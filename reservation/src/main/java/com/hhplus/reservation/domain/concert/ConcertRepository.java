package com.hhplus.reservation.domain.concert;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> getSchedules(Long concertId);

    List<ConcertSeat> getSeats(Long concertScheduleId);

    Long countSeatAvaliable(List<Long> seats);

    void updateSeatsStatusWithLock(List<Long> seats, ConcertSeatStatus status);

    void updateAvailableSeats(Long concertScheduledId, int seatsSize);

    void restoreAvailableSeats(Long concertScheduledId, int seatsSize);

    Long getTotalPrice(List<Long> seatIds);

    Concert getConcert(Long concertId);

    ConcertSchedule getConcertSchedule(Long concertScheduledId);

    void updateSeatsAvaliable(List<Long> seatIds, ConcertSeatStatus concertSeatStatus);

    List<ConcertSeat> getAvailableSeats(List<Long> seatIds);

    List<ConcertSeat> getAvailableSeatsWithLock(List<Long> seatIds);
}
