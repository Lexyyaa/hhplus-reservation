package com.hhplus.reservation.domain.reserve;

import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertRepository;
import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    @Transactional
    public ReservationInfo reserve(Long concertScheduleId, Long userId, Long totalPrice, List<Long> seats){
        Reservation currReservation = Reservation.create(userId, concertScheduleId, totalPrice,ReservationType.TEMP_RESERVED);
        Reservation reservation = reservationRepository.save(currReservation);

        List<ReservationSeat> currReservationSeats = ReservationSeat.createList(reservation.getId(), seats);
        reservationRepository.saveAll(currReservationSeats);

        return Reservation.convert(reservation);
    }

    @Transactional
    public ReservationInfo getReservation(long reservationId) {
        Reservation reservation = reservationRepository.findByIdWithLock(reservationId);
        return Reservation.convert(reservation);
    }

    public ReservationInfo confirmedReservation(ReservationInfo reservation){

        Reservation currReservation = Reservation.create(reservation);

        currReservation.setReserved(ReservationType.RESERVED);
        currReservation.setReservedAt(LocalDateTime.now());

        Reservation saveReservation = reservationRepository.save(currReservation);
        return Reservation.convert(saveReservation);
    }

    public void restoreReservation() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservation();

        for (Reservation reservation : expiredReservations) {
            List<Long> seatIds = reservationRepository.findByReservationId(reservation.getId());

            concertRepository.updateSeatsAvaliable(seatIds, ConcertSeatStatus.AVAILABLE);

            concertRepository.updateAvailableSeats(reservation.getConcertScheduleId(), seatIds.size());

            reservationRepository.delete(reservation);
            reservationRepository.deleteAll(seatIds);
        }
    }
}