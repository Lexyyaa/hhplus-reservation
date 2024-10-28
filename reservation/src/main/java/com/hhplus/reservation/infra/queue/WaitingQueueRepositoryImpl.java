package com.hhplus.reservation.infra.queue;

import com.hhplus.reservation.domain.queue.WaitingQueue;
import com.hhplus.reservation.domain.queue.WaitingQueueRepository;
import com.hhplus.reservation.domain.queue.WaitingQueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {
    private final JPAWaitingQueueRepository jPAWaitingQueueRepository;

    @Override
    public Optional<WaitingQueue> findWaitingQueueByUserId(Long userId) {
        return jPAWaitingQueueRepository.findWaitingQueue(userId,WaitingQueueStatus.WAITING);
    }
    @Override
    public WaitingQueue save(WaitingQueue queueToken) {
        System.out.println("save queue.userId().  :" + queueToken.getUserId());
        System.out.println("save queue.getToken().  :" + queueToken.getToken());

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
