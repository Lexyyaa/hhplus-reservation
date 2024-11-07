package com.hhplus.reservation.infra.queue;

import com.hhplus.reservation.domain.queue.WaitingQueue;
import com.hhplus.reservation.domain.queue.WaitingQueueRepository;
import com.hhplus.reservation.domain.queue.WaitingQueueStatus;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {


    private final JPAWaitingQueueRepository jPAWaitingQueueRepository;

    private static final String QUEUE_KEY = "waiting_queue";

    private final String WAIT_QUEUE_KEY = "waiting:";
    private final String PROCESS_QUEUE_KEY = "working:concert:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;


    @Override
    public Optional<WaitingQueue> findWaitingQueueByUserId(Long userId) {
        return jPAWaitingQueueRepository.findWaitingQueue(userId,WaitingQueueStatus.WAITING);
    }

    @Override
    public WaitingQueue save(WaitingQueue queueToken) {
        return jPAWaitingQueueRepository.save(queueToken);
    }


    @Override
    public Optional<WaitingQueue> findWaitingQueueByToken(Long userId,String queueToken) {
        return jPAWaitingQueueRepository.findWaitingQueue(userId,queueToken);
    }
    @Override
    public Long findMyWaitNum(LocalDateTime createdAt){
        return jPAWaitingQueueRepository.findMyWaitNum(createdAt);
    }

    @Override
    public Long countProgressToken() {
        return jPAWaitingQueueRepository.countProgressTokens();
    }

    @Override
    public List<WaitingQueue> findNextToken() {
        return jPAWaitingQueueRepository.findNextTokens();
    }


    @Override
    public void updateProcessToken(List<Long> queueList, LocalDateTime processedAt, LocalDateTime expiredAt) {
        jPAWaitingQueueRepository.updateProcessTokens(queueList,processedAt,expiredAt);
    }

    @Override
    public void updateExpireToken() {
        jPAWaitingQueueRepository.updateExpire(WaitingQueueStatus.EXPIRED,LocalDateTime.now());
    }
    @Override
    public boolean  validateToken(String token) {
        Optional<WaitingQueue> optional = jPAWaitingQueueRepository.findInProcessQueue(token);
        return optional.isPresent() ? true : false;
    }

    @Override
    public void updateTokenDone(String token) {
        jPAWaitingQueueRepository.updateTokenDone(token,LocalDateTime.now());
    }

}
