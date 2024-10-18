package com.hhplus.reservation.interfaces.scheduler;

import com.hhplus.reservation.domain.queue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueScheduler {
    private final WaitingQueueService waitingQueueService;

    @Scheduled(fixedRate = 60000)
    public void checkProcessToken() {
        waitingQueueService.updateProcessToken();
    }

    @Scheduled(fixedRate = 60000)
    public void checkTokenExpire() {
        waitingQueueService.updateExpireToken();
    }

}

