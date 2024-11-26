package com.hhplus.reservation.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaTestConsumer {

    private final List<KafkaMessage> events = new ArrayList<>();

    @KafkaListener(topics = "payment", groupId = "test-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(KafkaMessage event) {
        events.add(event);
    }

    public List<KafkaMessage> getReceivedEvents() {
        return events;
    }

    public void clearEvents() {
        events.clear();
    }
}
