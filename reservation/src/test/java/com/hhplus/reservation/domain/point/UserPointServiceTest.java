package com.hhplus.reservation.domain.point;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.domain.point.UserPointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.application.dto.UserPointInfo;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;

@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @InjectMocks
    private UserPointService userPointService;

    @Test
    @DisplayName("포인트 충전 성공")
    void 포인트_충전_성공() {
        Long userId = 1L;
        Long amount = 100L;

        UserPoint userPoint = UserPoint.builder().id(userId).point(0L).build();
        when(userPointRepository.findByUserIdWithLock(userId)).thenReturn(userPoint);
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);

        UserPointInfo result = userPointService.chargePoint(userId, amount);

        assertEquals(100L, result.getPoint());
        verify(userPointRepository).save(userPoint);
    }

    @Test
    @DisplayName("유효하지 않은 충전 금액")
    void 유효하지_않은_충전_금액() {
        Long userId = 1L;
        Long invalidAmount = 0L;

        BizException exception = assertThrows(BizException.class,
                () -> userPointService.chargePoint(userId, invalidAmount));

        assertEquals(ErrorType.INVALID_CHARGE_AMOUNT, exception.getErrorType());
    }

    @Test
    @DisplayName("포인트 조회 성공")
    void 포인트_조회_성공() {
        Long userId = 1L;
        UserPoint userPoint = UserPoint.builder().id(userId).point(200L).build();

        when(userPointRepository.findByUserId(userId)).thenReturn(userPoint);

        UserPointInfo result = userPointService.checkPoint(userId);

        assertEquals(200L, result.getPoint());
        verify(userPointRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("사용자 정보 없음")
    void 사용자_정보_없음() {
        Long userId = 99L;

        // 예외가 발생하도록 Mock 설정
        when(userPointRepository.findByUserId(userId))
                .thenThrow(new BizException(ErrorType.USER_NOT_FOUND));

        // BizException이 발생하는지 확인
        BizException exception = assertThrows(BizException.class,
                () -> userPointService.checkPoint(userId));

        // 예외의 타입이 정확한지 확인
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
    }


    @Test
    @DisplayName("포인트 사용 성공")
    void 포인트_사용_성공() {
        Long userId = 1L;
        Long price = 50L;

        UserPoint userPoint = UserPoint.builder().id(userId).point(100L).build();
        when(userPointRepository.findByUserIdWithLock(userId)).thenReturn(userPoint);
        when(userPointRepository.save(any(UserPoint.class))).thenReturn(userPoint);

        UserPointInfo result = userPointService.payPoint(userId, price);

        assertEquals(50L, result.getPoint());
        verify(userPointRepository).save(userPoint);
    }

    @Test
    @DisplayName("포인트 부족 예외")
    void 포인트_부족_예외() {
        Long userId = 1L;
        Long price = 150L;

        UserPoint userPoint = UserPoint.builder().id(userId).point(100L).build();
        when(userPointRepository.findByUserIdWithLock(userId)).thenReturn(userPoint);

        BizException exception = assertThrows(BizException.class,
                () -> userPointService.payPoint(userId, price));

        assertEquals(ErrorType.INSUFFICIENT_POINTS, exception.getErrorType());
    }
}
