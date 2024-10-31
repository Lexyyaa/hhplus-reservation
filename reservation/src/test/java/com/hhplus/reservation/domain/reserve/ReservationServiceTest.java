package com.hhplus.reservation.domain.reserve;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.reserve.*;
import com.hhplus.reservation.domain.concert.ConcertRepository;
import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("좌석 예약 성공")
    void 좌석_예약_성공() {
        Long userId = 1L;
        Long concertScheduleId = 100L;
        Long totalPrice = 150000L;
        List<Long> seatIds = List.of(1L, 2L);

        Reservation reservation = Reservation.create(userId, concertScheduleId, totalPrice, ReservationType.TEMP_RESERVED);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationInfo result = reservationService.reserve(concertScheduleId, userId, totalPrice, seatIds);

        assertNotNull(result);
        verify(reservationRepository).save(any(Reservation.class));
        verify(reservationRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("예약 내역 조회 성공")
    void 예약_내역_조회_성공() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).build();

        when(reservationRepository.findById(reservationId)).thenReturn(reservation);

        ReservationInfo result = reservationService.getReservation(reservationId);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
    }

    @Test
    @DisplayName("예약 내역이 존재하지 않음")
    void 예약_내역이_존재하지_않음() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId))
                .thenThrow(new BizException(ErrorType.RESERVATION_NOT_FOUND));

        BizException exception = assertThrows(BizException.class,
                () -> reservationService.getReservation(reservationId));

        assertEquals(ErrorType.RESERVATION_NOT_FOUND, exception.getErrorType());
    }

    @Test
    @DisplayName("예약 확정 성공")
    void 예약_확정_성공() {
        Long reservationId = 1L;
        ReservationInfo reservationInfo = ReservationInfo.builder()
                .id(reservationId)
                .userId(1L)
                .concertScheduleId(100L)
                .totalPrice(150000L)
                .build();

        Reservation confirmedReservation = Reservation.builder()
                .id(reservationId)
                .userId(reservationInfo.getUserId())
                .concertScheduleId(reservationInfo.getConcertScheduleId())
                .totalPrice(reservationInfo.getTotalPrice())
                .reserved(ReservationType.RESERVED)
                .reservedAt(LocalDateTime.now())
                .build();

        when(reservationRepository.confirmedReservationWithLock(reservationId)).thenReturn(confirmedReservation);

        ReservationInfo result = reservationService.confirmedReservation(reservationInfo);

        assertEquals(ReservationType.RESERVED, result.getReserved());
    }

    @Test
    @DisplayName("예약 만료된 내역 복원")
    void 예약_만료된_내역_복원() {
        Reservation expiredReservation = Reservation.builder()
                .id(1L)
                .concertScheduleId(100L)
                .build();

        List<Long> seatIds = List.of(1L, 2L);

        when(reservationRepository.findExpiredReservation()).thenReturn(List.of(expiredReservation));
        when(reservationRepository.findSeatsByReservationId(expiredReservation.getId())).thenReturn(seatIds);

        reservationService.restoreReservation();

        verify(concertRepository).updateSeatsAvaliable(seatIds, ConcertSeatStatus.AVAILABLE);
        verify(concertRepository).updateAvailableSeats(expiredReservation.getConcertScheduleId(), seatIds.size());
        verify(reservationRepository).delete(expiredReservation);
        verify(reservationRepository).deleteAll(seatIds);
    }

    @Test
    @DisplayName("이미 예약된 좌석 예외")
    void 이미_예약된_좌석_예외() {
        Long userId = 1L;
        Long concertScheduleId = 100L;
        Long totalPrice = 150000L;
        List<Long> seatIds = List.of(1L, 2L);

        when(reservationRepository.save(any(Reservation.class)))
                .thenThrow(new BizException(ErrorType.SEAT_ALREADY_RESERVED));

        BizException exception = assertThrows(BizException.class,
                () -> reservationService.reserve(concertScheduleId, userId, totalPrice, seatIds));

        assertEquals(ErrorType.SEAT_ALREADY_RESERVED, exception.getErrorType());
    }
}
