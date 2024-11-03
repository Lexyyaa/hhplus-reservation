package com.hhplus.reservation.infra.reservation;

import com.hhplus.reservation.domain.reserve.Reservation;
import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.domain.reserve.ReservationSeat;
import com.hhplus.reservation.domain.reserve.ReservationType;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JPAReservationRepository jpaReservationRepository;
    private final JPAReservationSeatRepository jpaReservationSeatRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return jpaReservationRepository.save(reservation);
    }

    @Override
    public List<ReservationSeat> saveAll(List<ReservationSeat> seats) {
        return jpaReservationSeatRepository.saveAll(seats);
    }

    @Override
    public Reservation findByIdWithLock(Long reservationId) {
        return jpaReservationRepository.findByIdWithLock(reservationId)
                .orElseThrow(() -> new BizException(ErrorType.RESERVATION_NOT_FOUND));
    }

    @Override
    public List<Long> findSeatsByReservationId(Long id) {
        return jpaReservationSeatRepository.findByReservationId(id);
    }

    @Override
    public List<Reservation> findExpiredReservation() {
        return jpaReservationRepository.findExpiredReservation(LocalDateTime.now());
    }

    @Override
    public void delete(Reservation reservation) {
        jpaReservationRepository.delete(reservation);
    }
    @Override
    public void deleteAll(List<Long> seatIds) {
        jpaReservationSeatRepository.deleteAllBySeatIds(seatIds);
    }

    @Override
    public Reservation findById(Long reservationId) {
        return jpaReservationRepository.findById(reservationId).orElseThrow(() -> new BizException(ErrorType.RESERVATION_NOT_FOUND));
    }

    @Override
    public List<Reservation> findAll() {
        return jpaReservationRepository.findAll();
    }

    @Override
    public Reservation confirmedReservationWithLock(Long reservationId) {
        try {
            Reservation reservation = jpaReservationRepository.findById(reservationId)
                    .orElseThrow(() -> new BizException(ErrorType.RESERVATION_NOT_FOUND));


            reservation.setReserved(ReservationType.RESERVED);
            reservation.setReservedAt(LocalDateTime.now());

            return jpaReservationRepository.save(reservation);

        } catch (OptimisticLockingFailureException e) {
            throw new BizException(ErrorType.PAYMENT_ALREADY_MADE);
        }
    }
}