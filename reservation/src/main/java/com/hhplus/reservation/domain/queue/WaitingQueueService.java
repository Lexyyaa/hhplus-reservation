package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.interfaces.dto.queue.WaitingQueuePollingResponse;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository queueRepository;

    /**
     * 토큰을 발급한다(대기열 진입)
     */
    @Transactional
    public WaitingQueueResponse getOrCreateQueueToken(Long userId) {
        Optional<WaitingQueue> optionalQueue = queueRepository.findWaitingQueueByUserId(userId);

        if (optionalQueue.isPresent()) {
            return WaitingQueue.convert(optionalQueue.get());
        }

        String token = WaitingQueue.makeToken(userId);
        WaitingQueue queue = WaitingQueue.builder()
                                        .userId(userId)
                                        .token(token)
                                        .status(WaitingQueueStatus.WAITING)
                                        .build();


        WaitingQueue newQueue = queueRepository.save(queue);
        return WaitingQueue.convert(newQueue);
    }


    /**
     * 나의 대기번호를 조회한다.
     */
    public WaitingQueuePollingResponse getQueueToken(Long userId, String queueToken) {
        Optional<WaitingQueue> optional = queueRepository.findWaitingQueueByToken(userId, queueToken);

        WaitingQueue.checkToken(optional.isPresent());

        Long waitNum = queueRepository.findMyWaitNum(optional.get().getCreatedAt());
        return WaitingQueuePollingResponse.builder()
                .waitNum(waitNum)
                .createdAt(optional.get().getCreatedAt()).build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 토큰이 유효한지 검증한다.
     */
    public void validateToken(String queueToken){
        boolean isValidToken = queueRepository.validateToken(queueToken);
        WaitingQueue.validateToken(isValidToken);
    }

    /**
     * 토큰을 실행처리한다.
     */
    @Transactional
    public void updateProcessToken() {
        Long currProgressCnt = queueRepository.countProgressToken();

        WaitingQueue.isValidCount(currProgressCnt);

        List<WaitingQueue> nextTokens = queueRepository.findNextToken();

        WaitingQueue.isValidTokenList(nextTokens);

        List<Long> queueList = nextTokens.stream()
                .map(WaitingQueue::getId)
                .collect(Collectors.toList());

        LocalDateTime currTime = LocalDateTime.now();
        queueRepository.updateProcessToken(queueList,currTime, currTime.plusMinutes(10));
    }

    /**
     * 토큰을 완료처리한다.
     */
    @Transactional
    public void updateTokenDone(String token){
        queueRepository.updateTokenDone(token);
    }

    /**
     * 토큰을 만료처리한다.
     */
    @Transactional
    public void updateExpireToken() {
        queueRepository.updateExpireToken();
    }
}
