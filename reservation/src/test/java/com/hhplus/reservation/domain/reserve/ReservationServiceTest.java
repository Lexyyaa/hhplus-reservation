package com.hhplus.reservation.domain.reserve;

import com.hhplus.reservation.application.dto.ReservationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약을 성공적으로 생성한다")
    void 예약을_성공적으로_생성한다() {
        ReservationInfo reservationInfo = ReservationInfo.builder()
                .userId(1L)
                .concertScheduleId(1L)
                .totalPrice(100L)
                .reserved(ReservationType.TEMP_RESERVED)
                .reserveRequestAt(LocalDateTime.now())
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        Reservation reservation = Reservation.create(reservationInfo);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationInfo result = reservationService.reserve(1L, 1L, 100L, List.of(1L, 2L));

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(100L, result.getTotalPrice());
    }

    @Test
    @DisplayName("예약 조회에 성공한다")
    void 예약_조회에_성공한다() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .userId(1L)
                .concertScheduleId(1L)
                .build();

        when(reservationRepository.findByIdWithLock(1L)).thenReturn(reservation);

        ReservationInfo result = reservationService.getReservation(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("예약 조회에 실패한다 - 예약이 존재하지 않을 때")
    void 예약_조회에_실패한다_예약이_존재하지_않을_때() {
        when(reservationRepository.findByIdWithLock(1L))
                .thenThrow(new RuntimeException("예약내역이 존재하지 않습니다."));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservationService.getReservation(1L)
        );

        assertEquals("예약내역이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("만료된 예약을 정리한다")
    void 만료된_예약을_정리한다() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertScheduleId(1L)
                .build();

        when(reservationRepository.findExpiredReservation()).thenReturn(List.of(reservation));

        assertDoesNotThrow(() -> reservationService.restoreReservation());
    }
}