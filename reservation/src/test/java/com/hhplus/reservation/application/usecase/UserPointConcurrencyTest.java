package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.domain.point.UserPointService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserPointConcurrencyTest {

    @Autowired
    private UserPointUsecase userPointUsecase;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final CountDownLatch latch = new CountDownLatch(3); 

    @Test
    @DisplayName("동시 포인트 충전 시도 - 한 번만 성공")
    void 동시_포인트_충전_한번만_성공() throws InterruptedException, ExecutionException {
        Long userId = 1L;
        Long amount = 5000L;

        List<Future<Boolean>> futures = List.of(
                executorService.submit(() -> chargePoint(userId, amount)),
                executorService.submit(() -> chargePoint(userId, amount)),
                executorService.submit(() -> chargePoint(userId, amount))
        );

        int successCount = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) { 
                successCount++;
            }
        }

        assertThat(successCount).isEqualTo(1);
    }

    Boolean chargePoint(Long userId, Long amount) {
        try {
            latch.countDown();
            latch.await();

            userPointUsecase.chargePoint(userId, amount);
            return true;
        } catch (BizException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
