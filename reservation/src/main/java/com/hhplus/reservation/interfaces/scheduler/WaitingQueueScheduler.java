package com.hhplus.reservation.interfaces.scheduler;

import com.hhplus.reservation.domain.queue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationRestoreScheduler {
	@Scheduled(fixedRate = 30000)
	private final ReservationService reservationService;

	public void checkProcessToken() {
		reservationService.restoreReservation();
	}
}
