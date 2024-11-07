package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.config.TestContainersConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ConcertServiceCacheTest extends TestContainersConfig {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertCacheRepository concertCacheRepository;

    @Test
    @DisplayName("캐시 미존재시 DB 조회 후 캐시에 저장")
    void 캐시_미존재시_DB_조회_후_캐시에_저장() {

        Long concertId = 2L;

        // 첫 번째 조회: 캐시 비어 있음
        List<ConcertScheduleInfo> schedulesFirstRead = concertService.getSchedules(concertId);
        assertThat(schedulesFirstRead).isNotEmpty();

        // 캐시에서 확인 - 첫 번째 조회 후 캐시에 저장되었는지 확인
        List<ConcertScheduleInfo> cachedSchedulesAfterFirstRead = concertCacheRepository.getCachedSchedules(concertId);
        assertThat(cachedSchedulesAfterFirstRead).isNotEmpty();
        assertThat(cachedSchedulesAfterFirstRead).isEqualTo(schedulesFirstRead);

        // 두 번째 조회: 캐시에서 데이터 가져옴
        List<ConcertScheduleInfo> schedulesSecondRead = concertService.getSchedules(concertId);
        assertThat(schedulesSecondRead).isEqualTo(cachedSchedulesAfterFirstRead);

        log.info("ConcertScheduleInfo : {}",schedulesSecondRead);
        for(ConcertScheduleInfo schedule : schedulesSecondRead){
            log.info("schedule : {}",schedule);
        }

        // 캐시가 실제로 사용되었는지 검증 (캐시에서 가져온 데이터가 동일한지 확인)
        assertThat(schedulesSecondRead).isEqualTo(schedulesFirstRead);
    }
}