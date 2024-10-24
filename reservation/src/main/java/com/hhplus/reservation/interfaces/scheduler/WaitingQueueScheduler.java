package com.hhplus.reservation.interfaces.scheduler;

import com.hhplus.reservation.domain.queue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationRestoreScheduler {

    private final ReservationService reservationService;
    @Scheduled(fixedRate = 10000)
    public void checkProcessToken() {
        reservationService.restoreReservation();
    }
}
