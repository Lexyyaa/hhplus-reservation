package com.hhplus.reservation.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaTestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String key, Object message) {
        kafkaTemplate.send("payment", key, message);
    }
}
