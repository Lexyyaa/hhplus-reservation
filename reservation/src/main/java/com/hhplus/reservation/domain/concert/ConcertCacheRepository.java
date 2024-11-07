package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;

import java.util.List;

public interface ConcertCacheRepository {

    List<ConcertScheduleInfo> getCachedSchedules(Long concertId);

    void cacheSchedules(Long concertId, List<ConcertScheduleInfo> schedules);

    void evictSchedulesCache(Long concertId);
}
