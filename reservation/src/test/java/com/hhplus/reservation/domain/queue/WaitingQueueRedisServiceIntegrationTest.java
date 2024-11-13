package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.config.TestContainersConfig;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueuePollingResponse;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class WaitingQueueRedisServiceIntegrationTest extends TestContainersConfig {

    @Autowired
    private WaitingQueueService waitingQueueRedisService;

    @Autowired
    private WaitingQueueRepository waitingQueueRedisRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    public void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    private final String testTokenForUserId1 = "MV9fYzRjYTQyMzgtYTBiOS0zMzgyLThkY2MtNTA5YTZmNzU4NDli";

    @Test
    @DisplayName("대기열_토큰_발급_조회")
    public void 대기열_토큰_발급_조회() {
        WaitingQueueResponse response = waitingQueueRedisService.getOrCreateQueueToken(1L);

        assertEquals(response.getToken(), testTokenForUserId1);
        assertEquals(waitingQueueRedisService.getQueueToken(testTokenForUserId1).getWaitNum(), 0);
    }

    @Test
    @DisplayName("대기번호_조회")
    public void 대기번호_조회() {

        waitingQueueRedisService.getOrCreateQueueToken((5L));
        waitingQueueRedisService.getOrCreateQueueToken((4L));
        waitingQueueRedisService.getOrCreateQueueToken((3L));
        waitingQueueRedisService.getOrCreateQueueToken((2L));
        waitingQueueRedisService.getOrCreateQueueToken((1L));

        WaitingQueuePollingResponse response = waitingQueueRedisService.getQueueToken(testTokenForUserId1);

        assertEquals(response.getWaitNum(), 4);
    }

    @Test
    @DisplayName("토큰_활성화_처리")
    public void 토큰_활성화_처리() {

        waitingQueueRedisService.getOrCreateQueueToken((6L));
        waitingQueueRedisService.getOrCreateQueueToken((5L));
        waitingQueueRedisService.getOrCreateQueueToken((4L));
        waitingQueueRedisService.getOrCreateQueueToken((3L));
        waitingQueueRedisService.getOrCreateQueueToken((2L));
        waitingQueueRedisService.getOrCreateQueueToken((1L));

        waitingQueueRedisService.updateActiveToken();

        assertEquals(waitingQueueRedisService.getQueueToken(testTokenForUserId1).getWaitNum(), 0);
    }


    @Test
    @DisplayName("토큰_유효성_검증")
    public void 토큰_유효성_검증() {

        waitingQueueRedisService.getOrCreateQueueToken((5L));
        waitingQueueRedisService.getOrCreateQueueToken((4L));
        waitingQueueRedisService.getOrCreateQueueToken((3L));
        waitingQueueRedisService.getOrCreateQueueToken((2L));
        waitingQueueRedisService.getOrCreateQueueToken((1L));

        waitingQueueRedisService.updateActiveToken();

        boolean isValid = waitingQueueRedisService.isValidToken("NF9fYTg3ZmY2NzktYTJmMy0zNzFkLTkxODEtYTY3Yjc1NDIxMjJj");
        assertTrue(isValid);
    }

    @Test
    @DisplayName("토큰_완료_처리")
    public void 토큰_완료_처리() {
        waitingQueueRedisService.getOrCreateQueueToken((5L));
        waitingQueueRedisService.getOrCreateQueueToken((4L));
        waitingQueueRedisService.getOrCreateQueueToken((3L));
        waitingQueueRedisService.getOrCreateQueueToken((2L));
        waitingQueueRedisService.getOrCreateQueueToken((1L));

        waitingQueueRedisService.updateActiveToken();

        waitingQueueRedisService.deleteToken(testTokenForUserId1);

        assertEquals(waitingQueueRedisRepository.getActiveCnt(),4);
    }
}