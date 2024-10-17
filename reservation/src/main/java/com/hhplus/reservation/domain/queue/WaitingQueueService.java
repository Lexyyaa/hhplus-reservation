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

    public WaitingQueueResponse makeQueueToken(Long userId) {


        Optional<WaitingQueue> optionalQueue = queueRepository.findWaitingQueueByUserId(userId);

        String token = WaitingQueue.makeToken(userId,optionalQueue);

        WaitingQueue queue = queueRepository.save(WaitingQueue.builder()
                                                .userId(userId)
                                                .token(token)
                                                .status(WaitingQueueStatus.WATING).build());
        return WaitingQueue.convert(queue);
    }

    public WaitingQueuePollingResponse getQueueToken(Long userId, String queueToken) {
        Optional<WaitingQueue> optional = queueRepository.findWaitingQueueByToken(userId, queueToken);
        WaitingQueue.checkToken(optional.isPresent());

        Long waitNum = queueRepository.findMyWaitNum(optional.get().getCreatedAt());
        return WaitingQueuePollingResponse.builder()
                .waitNum(waitNum)
                .createdAt(optional.get().getCreatedAt()).build();
    }

    public void validateToken(String queueToken){
        boolean isValidToken = queueRepository.validateToken(queueToken);
        WaitingQueue.validateToken(isValidToken);
    }

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

    public void updateTokenDone(String token){
        queueRepository.updateTokenDone(token);
    }
    @Transactional
    public void updateExpireToken() {
        queueRepository.updateExpireToken();
    }
}
