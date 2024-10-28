package com.hhplus.reservation.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {
    @Test
    @DisplayName("포인트를 충전한다")
    void 포인트를_충전한다() {
        UserPoint userPoint = new UserPoint();
        userPoint.chargePoint(100L);
        assertEquals(100L, userPoint.getPoint());
    }

    @Test
    @DisplayName("포인트로 결제한다")
    void 포인트로_결제한다() {
        UserPoint userPoint = new UserPoint();
        userPoint.chargePoint(100L);
        userPoint.payPoint(50L);
        assertEquals(50L, userPoint.getPoint());
    }
}