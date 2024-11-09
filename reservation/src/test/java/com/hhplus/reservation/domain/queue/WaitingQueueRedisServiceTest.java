package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.interfaces.dto.queue.WaitingQueuePollingResponse;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WaitingQueueRedisServiceTest {

    @InjectMocks
    private WaitingQueueRedisService waitingQueueRedisService;

    @Spy
    private WaitingQueueRedisRepository waitingQueueRedisRepository;

    private final String testTokenForUserId1 = "MV9fYzRjYTQyMzgtYTBiOS0zMzgyLThkY2MtNTA5YTZmNzU4NDli";

    @DisplayName("대기열_토큰_발급_성공")
    @Test
    public void 대기열_토큰_발급_성공() {
        Long userId = 1L;

        when(waitingQueueRedisRepository.findWaitingQueueByToken(testTokenForUserId1)).thenReturn(null);
        when(waitingQueueRedisRepository.addWaitingQueue(testTokenForUserId1)).thenReturn(testTokenForUserId1);

        WaitingQueueResponse response = waitingQueueRedisService.getOrCreateQueueToken(userId);

        assertNotNull(response);
        assertEquals(testTokenForUserId1, response.getToken());
    }

    @DisplayName("대기번호_조회_성공")
    @Test
    public void 대기번호_조회_성공() {
        String token = testTokenForUserId1;
        Long waitNum = 1L;

        when(waitingQueueRedisRepository.getWaitNum(token)).thenReturn(waitNum);

        WaitingQueuePollingResponse response = waitingQueueRedisService.getQueueToken(token);

        assertNotNull(response);
        assertEquals(waitNum, response.getWaitNum());
    }

    @DisplayName("대기번호_조회_실패")
    @Test
    public void 대기번호_조회_실패() {
        String token = testTokenForUserId1;

        when(waitingQueueRedisRepository.getWaitNum(token)).thenThrow(new BizException(ErrorType.TOKEN_NOT_FOUND));

        assertThrows(BizException.class, () -> waitingQueueRedisService.getQueueToken(token));
    }

    @DisplayName("토큰_활성화_처리_성공")
    @Test
    public void 토큰_활성화_처리_성공() {
        long currActiveCnt = 3L;
        int remainCnt = 2;
        List<String> tokensToActivate = List.of("사용자1", "사용자2");

        when(waitingQueueRedisRepository.getActiveCnt()).thenReturn(currActiveCnt);
        when(waitingQueueRedisRepository.popWaitingQueueToken(remainCnt)).thenReturn(tokensToActivate);

        waitingQueueRedisService.updateActiveToken();

        assertEquals(tokensToActivate, waitingQueueRedisRepository.popWaitingQueueToken(remainCnt));
        assertEquals(5, currActiveCnt + tokensToActivate.size());
    }

    @DisplayName("토큰_활성화_처리_실패_최대값_초과")
    @Test
    public void 토큰_활성화_처리_실패_최대값_초과() {
        long currActiveCnt = 5L;

        when(waitingQueueRedisRepository.getActiveCnt()).thenReturn(currActiveCnt);

        BizException exception = assertThrows(BizException.class,
                () -> waitingQueueRedisService.updateActiveToken());

        assertEquals(ErrorType.MAX_PROGRESS_EXCEEDED, exception.getErrorType());
    }

    @DisplayName("토큰_유효성_검증_성공")
    @Test
    public void 토큰_유효성_검증_성공() {
        String token = testTokenForUserId1;

        when(waitingQueueRedisRepository.getActiveToken(token)).thenReturn(testTokenForUserId1);

        boolean isValid = waitingQueueRedisService.isValidToken(token);

        assertTrue(isValid);
    }

    @DisplayName("토큰_유효성_검증_실패")
    @Test
    public void 토큰_유효성_검증_실패() {
        String token = testTokenForUserId1;

        when(waitingQueueRedisRepository.getActiveToken(token)).thenReturn(null);

        boolean isValid = waitingQueueRedisService.isValidToken(token);

        assertFalse(isValid);
    }

    @DisplayName("토큰_완료_처리_성공")
    @Test
    public void 토큰_완료_처리_성공() {
        String token = testTokenForUserId1;

        when(waitingQueueRedisRepository.deleteToken(token)).thenReturn(true);

        boolean isDeleteSuccees = waitingQueueRedisService.deleteToken(token);

        assertTrue(isDeleteSuccees);
    }
}
