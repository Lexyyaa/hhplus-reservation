package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.support.error.BizException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class WaitingQueueTest {

    @Test
    @DisplayName("사용자 ID로 토큰을 생성한다")
    void 사용자_ID로_토큰을_생성한다() {
        Long userId = 1L;
        String token = WaitingQueue.makeToken(userId);
        assertTrue(Base64.getDecoder().decode(token).length > 0);
    }

    @Test
    @DisplayName("유효한 토큰인지 확인한다")
    void 유효한_토큰인지_확인한다() {
        assertDoesNotThrow(() -> WaitingQueue.validateToken(true));
        assertThrows(BizException.class, () -> WaitingQueue.validateToken(false));
    }
}