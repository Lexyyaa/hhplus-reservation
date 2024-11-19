package com.hhplus.reservation.infra.payment;

import com.hhplus.reservation.domain.payment.PaymentDeleteTokenCommand;
import com.hhplus.reservation.domain.payment.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void deleteToken(Object paymentDeleteTokenCommand) {
        applicationEventPublisher.publishEvent(paymentDeleteTokenCommand);
    }
}
