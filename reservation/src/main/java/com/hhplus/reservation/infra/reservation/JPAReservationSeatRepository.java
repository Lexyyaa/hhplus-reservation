package com.hhplus.reservation.infra.reservation;

import com.hhplus.reservation.domain.reserve.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JPAReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    @Modifying
    @Query("DELETE FROM ReservationSeat rs WHERE rs.seatId IN :seatIds")
    void deleteAllBySeatIds(@Param("seatIds") List<Long> seatIds);

    @Query("SELECT rs.seatId FROM ReservationSeat rs WHERE rs.reservationId IN :reservationId")
    List<Long> findByReservationId(Long reservationId);
}
