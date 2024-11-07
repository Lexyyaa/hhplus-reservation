package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    @InjectMocks
    private WaitingQueueService waitingQueueService;

    @Test
    @DisplayName("성공적으로 토큰을 생성한다.")
    void 성공적으로_토큰을_생성한다() {
        Long userId = 1L;
        WaitingQueue queue = WaitingQueue.builder().userId(userId).token("test_token").build();

        when(waitingQueueRepository.findWaitingQueueByUserId(userId)).thenReturn(Optional.empty());
        when(waitingQueueRepository.save(any(WaitingQueue.class))).thenReturn(queue);

        WaitingQueueResponse response = waitingQueueService.getOrCreateQueueToken(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals("test_token", response.getToken());
    }

    @Test
    @DisplayName("토큰 조회에 실패한다 - 토큰이 존재하지 않을 때")
    void 토큰_조회에_실패한다_토큰이_존재하지_않을_때() {
        Long userId = 1L;
        String token = "invalid_token";

        when(waitingQueueRepository.findWaitingQueueByToken(userId, token)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> waitingQueueService.getQueueToken(userId, token)
        );

        assertEquals("대기중인 토큰이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("토큰 검증에 실패한다 - 유효하지 않은 토큰")
    void 토큰_검증에_실패한다_유효하지_않은_토큰() {
        String token = "invalid_token";
        when(waitingQueueRepository.validateToken(token)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> waitingQueueService.validateToken(token)
        );

        assertEquals("유효하지 않은 토큰입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("진행 가능한 최대 수를 초과하면 예외를 발생시킨다.")
    void 진행_가능한_최대_수를_초과하면_예외를_발생시킨다() {
        when(waitingQueueRepository.countProgressToken()).thenReturn(6L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> waitingQueueService.updateProcessToken()
        );

        assertEquals("진입 가능 수를 초과하였습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("성공적으로 토큰 만료를 처리한다.")
    void 성공적으로_토큰_만료를_처리한다() {
        assertDoesNotThrow(() -> waitingQueueService.updateExpireToken());
        verify(waitingQueueRepository).updateExpireToken();
    }
}