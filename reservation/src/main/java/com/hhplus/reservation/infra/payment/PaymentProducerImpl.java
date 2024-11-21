package com.hhplus.reservation.infra.payment;

import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.payment.PaymentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducerImpl implements PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String topic,PaymentEvent.DeleteToken event) {

        Message<PaymentEvent.DeleteToken> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, String.valueOf(event.getMessageKey()))
                .build();

        log.info("Sending message: {}", message);
        kafkaTemplate.send(message);
    }
}
