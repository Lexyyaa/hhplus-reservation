package com.hhplus.reservation.domain.queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {

    Optional<WaitingQueue> findWaitingQueueByUserId(Long userId);

    WaitingQueue save(WaitingQueue queueToken);

    Optional<WaitingQueue> findWaitingQueueByToken(Long userId,String queueToken);

    Long findMyWaitNum(LocalDateTime createdAt);

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    boolean validateToken(String token);

    void updateTokenDone(String token);

    Long countProgressToken();

    List<WaitingQueue> findNextToken();

    void updateProcessToken(List<Long> queueList, LocalDateTime processedAt, LocalDateTime expiredAt);

    void updateExpireToken();
}
