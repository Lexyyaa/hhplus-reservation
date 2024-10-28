package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JPAConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {

    @Query("select cs FROM ConcertSchedule cs WHERE cs.concertId = :concertId and cs.availableStatus = 'AVAILABLE' AND cs.performDate > CURRENT_DATE")
    List<ConcertSchedule> getSchedules(Long concertId);

    @Modifying
    @Query("UPDATE ConcertSchedule cs SET cs.availableSeatNum = cs.availableSeatNum - :count WHERE cs.id = :scheduleId")
    void updateAvailableSeats(@Param("scheduleId") Long scheduleId, @Param("count") int count);

    @Modifying
    @Query("UPDATE ConcertSchedule cs SET cs.availableSeatNum = cs.availableSeatNum + :count WHERE cs.id = :scheduleId")
    void restoreAvailableSeats(@Param("scheduleId") Long scheduleId, @Param("count") int count);
}