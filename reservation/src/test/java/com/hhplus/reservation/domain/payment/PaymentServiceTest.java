package com.hhplus.reservation.domain.payment;

import com.hhplus.reservation.application.dto.ReservationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("결제 검증에 성공한다.")
    void 결제_검증에_성공한다() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(100L)
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        when(paymentRepository.existsByReservationId(reservation.getId())).thenReturn(false);

        assertDoesNotThrow(() -> paymentService.validatePayment(reservation));
    }

    @Test
    @DisplayName("결제 검증에 실패한다 - 만료된 예약")
    void 결제_검증에_실패한다_만료된_예약() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .reserveExpiredAt(LocalDateTime.now().minusMinutes(1))
                .build();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.validatePayment(reservation)
        );

        assertEquals("결제 시간이 초과되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 검증에 실패한다 - 이미 결제된 예약")
    void 결제_검증에_실패한다_이미_결제된_예약() {
        ReservationInfo reservation = ReservationInfo.builder().id(1L).build();

        when(paymentRepository.existsByReservationId(reservation.getId())).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.validatePayment(reservation)
        );

        assertEquals("결제된 예약입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("결제 정보를 성공적으로 저장한다.")
    void 결제_정보를_성공적으로_저장한다() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(100L)
                .build();

        Payment payment = Payment.builder()
                .userId(1L)
                .reservationId(reservation.getId())
                .price(reservation.getTotalPrice())
                .paidAt(LocalDateTime.now())
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.savePayment(1L, reservation);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(100L, result.getPrice());
    }
}
