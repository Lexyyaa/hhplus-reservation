package com.hhplus.reservation.infra.queue;

import com.hhplus.reservation.domain.queue.WaitingQueueRedisRepository;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepositoryImpl implements WaitingQueueRedisRepository {

    private static final String WAIT_QUEUE_KEY = "WAITING_QUEUE";
    private static final String ACTIVE_PREFIX = "ACTIVE:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;
    private final ValueOperations<String, String> valueOperations;

    /**
     * 토큰을 대기열에서 가져온다.
     */
    public Double findWaitingQueueByToken(String token) {
        return zSetOperations.score(WAIT_QUEUE_KEY, token);
    }

    /**
     * 대기열에 토큰을 추가한다.
     */
    public String addWaitingQueue(String token) {
        long score = System.currentTimeMillis();
        zSetOperations.add(WAIT_QUEUE_KEY, token, score);
        return token;
    }

    /**
     * 대기열에서 토큰의 순위를 가져온다.
     */
    public Long getWaitNum(String token) {
        return Optional.ofNullable(zSetOperations.rank(WAIT_QUEUE_KEY, token)).orElseThrow(
                () -> new BizException(ErrorType.TOKEN_NOT_FOUND)
        );
    }

    /**
     * 대기열에서 지정된 개수만큼 토큰 제거 후 반환한다.
     */
    public List<String> popWaitingQueueToken(int popCnt){
        List<String> tokenList = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<String>> tokenSet = zSetOperations.popMin(WAIT_QUEUE_KEY, popCnt);

        for (ZSetOperations.TypedTuple<String> token : tokenSet) {
            tokenList.add(token.getValue());
        }
        return tokenList;
    }

    /**
     * 활성 사용자 추가
     */
    public void addActiveQueue(List<String> tokenList) {
        for(String token : tokenList) {
            String key = ACTIVE_PREFIX + token;
            valueOperations.set(key, token, 300, TimeUnit.SECONDS);
        }
    }

    /**
     * 활성 사용자 개수 확인
     */
    public Long getActiveCnt() {
        Set<String> keys = redisTemplate.keys(ACTIVE_PREFIX + "*");
        return (keys != null) ? keys.size() : 0L;
    }


    /**
     * 활성화 큐에서 토큰이 존재하는지 확인하여 존재한다면 토큰을 반환한다.
     */
    public String getActiveToken(String token) {
        String key = ACTIVE_PREFIX + token;

        String queueToken = valueOperations.get(key);

        if(queueToken != null) {
            return queueToken;
        }

        return null;
    }

    /**
     * 활성 사용자 토큰 삭제
     */
    public boolean deleteToken(String token) {
        String key = ACTIVE_PREFIX + token;
        return redisTemplate.delete(key);
    }
}
