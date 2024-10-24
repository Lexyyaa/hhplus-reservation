package com.hhplus.reservation.domain.payment;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.payment.Payment;
import com.hhplus.reservation.domain.payment.PaymentRepository;
import com.hhplus.reservation.domain.payment.PaymentService;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 금액 검증 성공")
    void 결제_금액_검증_성공() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(50000L)
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(paymentRepository.existsByReservationId(reservation.getId())).thenReturn(false);

        assertDoesNotThrow(() -> paymentService.validatePayment(reservation));
        verify(paymentRepository).existsByReservationId(reservation.getId());
    }

    @Test
    @DisplayName("결제 만료 예외")
    void 결제_만료_예외() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(50000L)
                .reserveExpiredAt(LocalDateTime.now().minusMinutes(5))
                .build();

        BizException exception = assertThrows(BizException.class,
                () -> paymentService.validatePayment(reservation));

        assertEquals(ErrorType.PAYMENT_EXPIRED, exception.getErrorType());
    }

    @Test
    @DisplayName("잘못된 결제 금액 예외")
    void 잘못된_결제_금액_예외() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(-1000L)
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        BizException exception = assertThrows(BizException.class,
                () -> paymentService.validatePayment(reservation));

        assertEquals(ErrorType.INVALID_PAYMENT_AMOUNT, exception.getErrorType());
    }

    @Test
    @DisplayName("이미 결제된 예약 예외")
    void 이미_결제된_예약_예외() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(50000L)
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(paymentRepository.existsByReservationId(reservation.getId())).thenReturn(true);

        BizException exception = assertThrows(BizException.class,
                () -> paymentService.validatePayment(reservation));

        assertEquals(ErrorType.PAYMENT_ALREADY_MADE, exception.getErrorType());
    }

    @Test
    @DisplayName("결제 저장 성공")
    void 결제_저장_성공() {
        ReservationInfo reservation = ReservationInfo.builder()
                .id(1L)
                .totalPrice(50000L)
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
        assertEquals(50000L, result.getPrice());
        verify(paymentRepository).save(any(Payment.class));
    }
}
