package com.hhplus.reservation.domain.payment;

public interface PaymentEventPublisher {

    // 결제성공 시  토큰 삭제
    void deleteToken(PaymentEvent.DeleteToken event);
}
