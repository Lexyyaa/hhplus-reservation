package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.infra.reservation.JPAReservationRepository;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationUsecaseTest {

    @Autowired
    private ReservationUsecase reservationUsecase;

    @Autowired
    private JPAReservationRepository reservationRepository;

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
    }

    @Test
    @DisplayName("예약 성공 테스트")
    @Transactional
    void 예약_성공_테스트() {
        // given
        Long concertScheduleId = 1L;
        Long userId = 1L;
        List<Long> seats = List.of(1L, 2L);
        Long totalPrice = 60000L;

        ReserveSeatRequest request = new ReserveSeatRequest(userId, seats);

        // when
        ReserveResponse response = reservationUsecase.reserve(concertScheduleId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("이미 예약된 좌석 예외 테스트")
    @Transactional
    void 이미_예약된_좌석_예외_테스트() {

        Long concertScheduleId = 1L;
        Long userId = 1L;
        List<Long> seats = List.of(1L, 2L);


        ReserveSeatRequest request = new ReserveSeatRequest(userId, seats);
        reservationUsecase.reserve(concertScheduleId, request);

        // when & then
        ReserveSeatRequest duplicateRequest = new ReserveSeatRequest(userId, seats);
        assertThrows(RuntimeException.class,
                () -> reservationUsecase.reserve(concertScheduleId, duplicateRequest));
    }
}