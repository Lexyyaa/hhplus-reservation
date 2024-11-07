package com.hhplus.reservation.infra.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.domain.concert.ConcertCacheRepository;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertCacheRedisRepositoryImpl implements ConcertCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String SCHEDULE_KEY_PREFIX = "concert:schedule:";

    @Override
    public List<ConcertScheduleInfo> getCachedSchedules(Long concertId) {
        String cacheKey = SCHEDULE_KEY_PREFIX + concertId;

        String jsonData = redisTemplate.opsForValue().get(cacheKey);
        if (jsonData == null) {
            return Collections.emptyList();
        }

        try {
            ConcertScheduleInfo[] scheduleArray = objectMapper.readValue(jsonData, ConcertScheduleInfo[].class);
            return List.of(scheduleArray);
        } catch (Exception e) {
            throw new BizException(ErrorType.BAD_REQUEST);
        }
    }

    @Override
    public void cacheSchedules(Long concertId, List<ConcertScheduleInfo> schedules) {
        String cacheKey = SCHEDULE_KEY_PREFIX + concertId;
        try {
            String jsonData = objectMapper.writeValueAsString(schedules);
            redisTemplate.opsForValue().set(cacheKey, jsonData, Duration.ofHours(1));
        } catch (Exception e) {
            throw new BizException(ErrorType.BAD_REQUEST);
        }
    }

    @Override
    public void evictSchedulesCache(Long concertId) {
        String cacheKey = SCHEDULE_KEY_PREFIX + concertId;
        redisTemplate.delete(cacheKey);
    }
}
