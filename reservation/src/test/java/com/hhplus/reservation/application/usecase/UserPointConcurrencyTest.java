package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.domain.point.UserPointService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserPointConcurrencyTest {

    @Autowired
    private UserPointService userPointService;

    @Autowired
    private UserPointRepository userPointRepository;

    // 충전 동시성 테스트
}
