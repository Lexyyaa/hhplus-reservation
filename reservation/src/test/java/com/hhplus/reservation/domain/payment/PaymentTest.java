package com.hhplus.reservation.domain.payment;

import com.hhplus.reservation.support.error.BizException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    @Test
    @DisplayName("결제 시간이 만료되었는지 확인한다")
    void 결제_시간이_만료되었는지_확인한다() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        assertThrows(BizException.class, () -> Payment.isExpiredPayment(past));
    }

    @Test
    @DisplayName("유효한 결제 금액인지 확인한다")
    void 유효한_결제_금액인지_확인한다() {
        assertThrows(BizException.class, () -> Payment.isValidPrice(-1L));
    }

}