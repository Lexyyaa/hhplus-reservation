package com.hhplus.reservation.domain.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    private UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = UserPoint.builder()
                .id(1L)
                .name("TestUser")
                .point(100L)  // 초기 포인트 설정
                .build();
    }

    @Test
    @DisplayName("포인트를 충전한다")
    void 포인트를_충전한다() {
        userPoint.chargePoint(100L);
        assertEquals(200L, userPoint.getPoint());
    }

    @Test
    @DisplayName("포인트로 결제한다")
    void 포인트로_결제한다() {
        userPoint.payPoint(50L);
        assertEquals(50L, userPoint.getPoint());
    }
}