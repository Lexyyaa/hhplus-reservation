package com.hhplus.reservation.domain.payment;

public interface PaymentProducer {

    void send(String topic,PaymentEvent.DeleteToken event);
}
