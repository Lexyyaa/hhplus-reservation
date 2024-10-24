package com.hhplus.reservation.infra.reservation;

import com.hhplus.reservation.domain.reserve.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JPAReservationRepository extends JpaRepository<Reservation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.id = :reservationId")
    Optional<Reservation> findByIdWithLock(@Param("reservationId") Long reservationId);

    @Query("SELECT r FROM Reservation r WHERE r.reserveExpiredAt < :currTime AND r.reserved = 'TEMP_RESERVED'")
    List<Reservation> findExpiredReservation(@Param("currTime") LocalDateTime currTime);
}
