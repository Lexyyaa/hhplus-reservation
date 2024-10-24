package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.payment.PaymentRepository;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.infra.payment.JPAPaymentRepository;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentUsecaseTest {

    @Autowired
    private PaymentUsecase paymentUsecase;

    @Autowired
    private JPAPaymentRepository paymentRepository;

    @AfterEach
    void tearDown() {
        // 테스트 완료 후 결제 내역 삭제
        paymentRepository.deleteAll();
    }

    @Test
    @DisplayName("결제 성공")
    @Transactional
    void 결제_성공() {
        // given
        String token = "valid_token";
        Long reservationId = 1L;
        Long userId = 1L;

        // when
        PaymentResponse response = paymentUsecase.pay(token, reservationId, userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getPrice()).isEqualTo(40000); // 예시 금액
    }

    @Test
    @DisplayName("이미 결제된 예약 예외")
    @Transactional
    void 이미_결제된_예약_예외() {
        // given
        String token = "valid_token";
        Long reservationId = 1L;
        Long userId = 1L;

        // 첫 번째 결제 성공
        paymentUsecase.pay(token, reservationId, userId);

        // when & then
        BizException exception = assertThrows(BizException.class,
                () -> paymentUsecase.pay(token, reservationId, userId));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PAYMENT_ALREADY_MADE);
    }

    @Test
    @DisplayName("포인트 부족 예외")
    @Transactional
    void 포인트_부족_예외() {
        // given
        String token = "valid_token";
        Long reservationId = 2L; // 예약된 금액이 유저의 포인트보다 큼
        Long userId = 2L; // 포인트가 적은 유저

        BizException exception = assertThrows(BizException.class,
                () -> paymentUsecase.pay(token, reservationId, userId));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.INSUFFICIENT_POINTS);
    }

    @Test
    @DisplayName("만료된 예약 예외")
    @Transactional
    void 만료된_예약_예외() {
        String token = "valid_token";
        Long reservationId = 3L;
        Long userId = 3L;

        BizException exception = assertThrows(BizException.class,
                () -> paymentUsecase.pay(token, reservationId, userId));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PAYMENT_EXPIRED);
    }

}