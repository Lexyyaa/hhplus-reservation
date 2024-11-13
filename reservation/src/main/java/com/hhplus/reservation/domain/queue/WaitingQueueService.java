package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.interfaces.dto.queue.WaitingQueuePollingResponse;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRedisRepository;

    /**
     * 토큰을 발급한다(대기열 진입)
     */
    public WaitingQueueResponse getOrCreateQueueToken(Long userId) {
        String token = WaitingQueue.makeToken(userId);
        Double score = waitingQueueRedisRepository.findWaitingQueueByToken(token);

        if (score == null) {
            waitingQueueRedisRepository.addWaitingQueue(token);
        }

        WaitingQueue queue = WaitingQueue.builder()
                .token(token)
                .build();
        return WaitingQueue.convert(queue);
    }

    /**
     * 나의 대기번호를 조회한다.
     */
    public WaitingQueuePollingResponse getQueueToken(String queueToken) {
        Long waitNum = waitingQueueRedisRepository.getWaitNum(queueToken);
        return WaitingQueuePollingResponse.builder()
                .waitNum(waitNum).build();
    }

    /**
     * 토큰을 실행처리한다.
     */
    public void updateActiveToken() {
        long currActiveCnt = waitingQueueRedisRepository.getActiveCnt();
        log.info("currActiveCnt : {} ",currActiveCnt);

        WaitingQueue.isValidCount(currActiveCnt);

        int remainCnt = 5 - (int) currActiveCnt;

        List<String> tokenList = new ArrayList<>();
        tokenList = waitingQueueRedisRepository.popWaitingQueueToken(remainCnt);

        waitingQueueRedisRepository.addActiveQueue(tokenList);
    }

    /**
     * 토큰이 유효한지 검증한다.
     */
    public boolean isValidToken(String queueToken){
        String token = waitingQueueRedisRepository.getActiveToken(queueToken);
        return token == null? false: true ;
    }

    /**
     * 토큰을 완료처리한다.
     */
    @Transactional
    public boolean deleteToken(String token){
       return waitingQueueRedisRepository.deleteToken(token);
    }
}
