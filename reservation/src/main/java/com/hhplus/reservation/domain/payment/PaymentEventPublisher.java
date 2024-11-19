package com.hhplus.reservation.domain.payment;

public interface PaymentEventPublisher {

    // 예약성공 시  토큰 삭제
    void deleteToken(Object paymentDeleteTokenCommand);
}
