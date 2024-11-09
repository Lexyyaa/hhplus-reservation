package com.hhplus.reservation.domain.queue;

import java.util.List;

public interface WaitingQueueRedisRepository {

    Double findWaitingQueueByToken(String token);

    String addWaitingQueue(String token);

    Long getWaitNum(String token);

    Long getActiveCnt();

    List<String> popWaitingQueueToken(int popCnt);

    String getActiveToken(String token);

    void addActiveQueue(List<String> tokenList);

    boolean deleteToken(String token) ;
}
