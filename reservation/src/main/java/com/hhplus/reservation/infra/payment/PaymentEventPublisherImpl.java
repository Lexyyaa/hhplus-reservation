package com.hhplus.reservation.infra.payment;

import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.payment.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void deleteToken(PaymentEvent.DeleteToken event) {
        applicationEventPublisher.publishEvent(event);
    }
}
