package com.hhplus.reservation.domain.point;

import com.hhplus.reservation.application.dto.UserPointInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
class UserPointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @InjectMocks
    private UserPointService userPointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("포인트 충전에 성공한다")
    void 포인트_충전에_성공한다() {
        Long userId = 1L;
        Long amount = 100L;
        UserPoint userPoint = UserPoint.builder().id(userId).point(50L).build();

        when(userPointRepository.findByUserId(userId)).thenReturn(userPoint);
        when(userPointRepository.save(userPoint)).thenReturn(userPoint);

        UserPointInfo result = userPointService.chargePoint(userId, amount);

        assertNotNull(result);
        assertEquals(150L, result.getPoint());
    }

    @Test
    @DisplayName("포인트 충전에 실패한다 - 충전 금액이 0보다 작을 때")
    void 포인트_충전에_실패한다_충전_금액이_0보다_작을_때() {
        Long amount = -100L;

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> UserPoint.isValidAmount(amount)
        );

        assertEquals("충전 포인트는 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 조회에 성공한다")
    void 포인트_조회에_성공한다() {
        Long userId = 1L;
        UserPoint userPoint = UserPoint.builder().id(userId).point(100L).build();

        when(userPointRepository.findByUserId(userId)).thenReturn(userPoint);

        UserPointInfo result = userPointService.checkPoint(userId);

        assertNotNull(result);
        assertEquals(100L, result.getPoint());
    }

    @Test
    @DisplayName("포인트 결제에 실패한다 - 포인트가 부족할 때")
    void 포인트_결제에_실패한다_포인트가_부족할_때() {
        Long userId = 1L;
        Long price = 200L;
        UserPoint userPoint = UserPoint.builder().id(userId).point(100L).build();

        when(userPointRepository.findByUserIdWithLock(userId)).thenReturn(userPoint);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userPointService.payPoint(userId, price)
        );

        assertEquals("포인트가 부족합니다.", exception.getMessage());
    }
}