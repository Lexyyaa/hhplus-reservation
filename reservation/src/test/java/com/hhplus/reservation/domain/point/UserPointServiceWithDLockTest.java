package com.hhplus.reservation.domain.point;

import com.hhplus.reservation.application.dto.UserPointInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointServiceWithDLockTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private UserPointService userPointService;

    @Test
    @DisplayName("락_획득_포인트_충전_성공")
    void 락_획득_포인트_충전_성공() throws InterruptedException {

        Long userId = 1L;
        Long amount = 5000L;
        UserPoint userPoint = new UserPoint(userId,"유저1",1000L,1);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(userPointRepository.findByUserId(userId)).thenReturn(userPoint);
        when(userPointRepository.save(any(UserPoint.class))).thenAnswer((Answer<UserPoint>) invocation -> invocation.getArgument(0));

        UserPointInfo usePointInfo = userPointService.chargePointWithDLock(userId, amount);

        assertEquals(6000L, usePointInfo.getPoint());
    }

    @Test
    @DisplayName("락_획득_실패_포인트_충전_실패")
    void 락_획득_실패_포인트_충전_실패() throws InterruptedException {
        Long userId = 1L;
        Long amount = 5000L;

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> userPointService.chargePointWithDLock(userId, amount));
    }
}