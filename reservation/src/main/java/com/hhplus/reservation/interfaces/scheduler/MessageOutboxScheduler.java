package com.hhplus.reservation.interfaces.scheduler;

import com.hhplus.reservation.application.usecase.MessageOutboxUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

 @Component
 @RequiredArgsConstructor
public class MessageOutboxScheduler {
     private final MessageOutboxUsecase messageOutboxUsecase;

    @Scheduled(cron = "0 */5 * * * *")
    public void run() {
        messageOutboxUsecase.retryDeleteToken();
    }
}
