package com.hhplus.reservation.domain.reserve;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationSeat> saveAll(List<ReservationSeat> seats);

    Reservation findByIdWithLock(Long reservationId);

    List<Long> findSeatsByReservationId(Long id);

    List<Reservation> findExpiredReservation();

    void delete(Reservation reservation);

    void deleteAll(List<Long> seatIds);

    Reservation findById(Long reservationId);

    Reservation confirmedReservationWithLock(Long reservationId);

    List<Reservation> findAll();
}
