package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.ConcertSeat;
import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JPAConcertSeatRepository extends JpaRepository<ConcertSeat, Long>{


    @Query("select cs FROM ConcertSeat cs WHERE cs.concertScheduleId = :concertScheduleId and cs.status = 'AVAILABLE'")
    List<ConcertSeat> getSeats(@Param("concertScheduleId")Long concertScheduleId);

    @Query("select cs FROM ConcertSeat cs WHERE cs.id IN :seatIds")
    List<ConcertSeat> getSeatsWithLock(@Param("seatIds") List<Long> seatIds);

    @Query("SELECT COUNT(cs) FROM ConcertSeat cs WHERE cs.id IN :seatIds AND cs.status = 'AVAILABLE'")
    Long countSeatAvaliable(@Param("seatIds") List<Long> seatIds);

    @Query("SELECT cs FROM ConcertSeat cs WHERE cs.id IN :seatIds")
    List<ConcertSeat> countSeats(@Param("seatIds") List<Long> seatIds);

    @Query("SELECT SUM(cs.seatPrice) FROM ConcertSeat cs WHERE cs.id IN :seatIds")
    Long getTotalPrice(@Param("seatIds") List<Long> seatIds);

    @Modifying
    @Query("UPDATE ConcertSeat cs SET cs.status = :status WHERE cs.id IN :seatIds")
    void updateSeatsAvaliable(@Param("seatIds") List<Long> seatIds, @Param("status") ConcertSeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs FROM ConcertSeat cs WHERE cs.status = 'AVAILABLE' AND cs.id IN :seatIds")
    List<ConcertSeat>findAvailableSeatsByIdsWithLock(@Param("seatIds") List<Long> seatIds);

    @Query("select cs FROM ConcertSeat cs WHERE cs.status = 'AVAILABLE' AND cs.id IN :seatIds")
    List<ConcertSeat>findAvailableSeatsByIds(@Param("seatIds") List<Long> seatIds);


}





