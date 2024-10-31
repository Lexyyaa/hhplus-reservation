package com.hhplus.reservation.domain.reserve;

import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertRepository;
import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final RedissonClient redissonClient;

    /**
     * 좌석을 예약한다.
     */
    @Transactional
    public ReservationInfo reserve(Long concertScheduleId, Long userId, Long totalPrice, List<Long> seats){
        Reservation currReservation = Reservation.create(userId, concertScheduleId, totalPrice,ReservationType.TEMP_RESERVED);
        Reservation reservation = reservationRepository.save(currReservation);

        List<ReservationSeat> currReservationSeats = ReservationSeat.createList(reservation.getId(), seats);
        reservationRepository.saveAll(currReservationSeats);

        return Reservation.convert(reservation);
    }

    public ReservationInfo reserveWithDLock(Long concertScheduleId, Long userId, Long totalPrice, List<Long> seats){
        String lockKey = "reservationLock:" + concertScheduleId + ":" + seats.hashCode();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                log.info("Reservation lock start");
                try {
                    Reservation currReservation = Reservation.create(userId, concertScheduleId, totalPrice, ReservationType.TEMP_RESERVED);
                    Reservation reservation = reservationRepository.save(currReservation);

                    List<ReservationSeat> currReservationSeats = ReservationSeat.createList(reservation.getId(), seats);
                    reservationRepository.saveAll(currReservationSeats);

                    return Reservation.convert(reservation);
                } finally {
                    log.info("Reservation unLock");
                    lock.unlock();
                }
            } else {
                throw new IllegalStateException("락 획득 실패");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("인터럽트 발생", e);
        }
    }

    /**
     * 예약정보를 반환한다.
     */
    @Transactional
    public ReservationInfo getReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        return Reservation.convert(reservation);
    }

    /**
     * 예약을 확정한다.
     */
    @Transactional
    public ReservationInfo confirmedReservation(ReservationInfo reservation){
        Reservation saveReservation = reservationRepository.confirmedReservationWithLock(reservation.getId());
        return Reservation.convert(saveReservation);
    }

    /**
     * 예약내역을 복원한다(예약만료)
     */
    @Transactional
    public void restoreReservation() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservation();

        for (Reservation reservation : expiredReservations) {
            List<Long> seatIds = reservationRepository.findSeatsByReservationId(reservation.getId());

            concertRepository.updateSeatsAvaliable(seatIds, ConcertSeatStatus.AVAILABLE);

            concertRepository.updateAvailableSeats(reservation.getConcertScheduleId(), seatIds.size());

            reservationRepository.delete(reservation);
            reservationRepository.deleteAll(seatIds);
        }
    }
}