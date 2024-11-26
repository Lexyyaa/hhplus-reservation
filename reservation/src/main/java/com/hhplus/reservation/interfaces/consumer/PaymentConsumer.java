package com.hhplus.reservation.interfaces.consumer;

import com.hhplus.reservation.domain.message.MessageOutboxService;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final WaitingQueueService waitingQueueService;
    private final MessageOutboxService messageOutboxService;

    @KafkaListener(topics = "payment-completed-event-v1", groupId = "delete-token")
    public void deleteToken(ConsumerRecord<String, String> record){
        PaymentEvent.DeleteToken event = PaymentEvent.DeleteToken.convertToEvent(record.value());
        waitingQueueService.deleteToken(event.getToken());
    }

    @KafkaListener(topics = "payment-completed-event-v1", groupId = "payment-outbox")
    public void chkPublished(ConsumerRecord<String, String> record) {
        PaymentEvent.DeleteToken event = PaymentEvent.DeleteToken.convertToEvent(record.value());
        messageOutboxService.chkPublished(event.getMessageKey(), String.valueOf(event.getEventType()));
    }
}
