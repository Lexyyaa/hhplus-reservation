package com.hhplus.reservation.interfaces.event;

import com.hhplus.reservation.domain.payment.PaymentDeleteTokenCommand;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDeleteEventListener {

    private final WaitingQueueService waitingQueueService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteToken(PaymentDeleteTokenCommand paymentDeleteTokenCommand) {
        waitingQueueService.deleteToken(paymentDeleteTokenCommand.getToken());
    }
}
