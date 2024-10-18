package com.hhplus.reservation.interfaces.scheduler;

import com.hhplus.reservation.domain.reserve.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class ReservationRestoreScheduler {
//
//    private final ReservationService reservationService;
//    @Scheduled(fixedRate = 60000)
//    public void checkProcessToken() {
//        reservationService.restoreReservation();
//    }
//}
//
