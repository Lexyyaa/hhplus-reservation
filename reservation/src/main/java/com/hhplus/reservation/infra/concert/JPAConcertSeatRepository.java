package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.ConcertSeat;
import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JPAConcertSeatRepository extends JpaRepository<ConcertSeat, Long>{


    @Query("select cs FROM ConcertSeat cs WHERE cs.concertScheduleId = :concertScheduleId and cs.status = 'AVAILABLE'")
    List<ConcertSeat> getSeats(Long concertScheduleId);

    @Query("SELECT COUNT(cs) FROM ConcertSeat cs WHERE cs.id IN :seatIds AND cs.status = 'AVAILABLE'")
    Long countSeatAvaliable(@Param("seatIds") List<Long> seatIds);

    @Query("SELECT SUM(cs.seatPrice) FROM ConcertSeat cs WHERE cs.id IN :seatIds")
    Long getTotalPrice(@Param("seatIds") List<Long> seatIds);

    @Modifying
    @Query("UPDATE ConcertSeat cs SET cs.status = :status WHERE cs.id IN :seatIds")
    void updateSeatsAvaliable(@Param("seatIds") List<Long> seatIds, @Param("status") ConcertSeatStatus status);

}





