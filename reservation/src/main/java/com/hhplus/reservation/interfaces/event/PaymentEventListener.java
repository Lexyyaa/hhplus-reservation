package com.hhplus.reservation.interfaces.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxService;
import com.hhplus.reservation.domain.message.MessageStatus;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.payment.PaymentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final MessageOutboxService messageOutboxService;
    private final PaymentProducer paymentProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveMessageOutbox(PaymentEvent.DeleteToken event) {
        try{
            MessageOutbox message = MessageOutbox.builder()
                    .eventId(event.getMessageKey())
                    .eventType(PaymentEvent.EventType.DELETE_TOKEN)
                    .domainName("payment")
                    .topic("payment-completed-event-v1")
                    .payload(objectMapper.writeValueAsString(event))
                    .eventStatus(MessageStatus.INIT)
                    .build();

            messageOutboxService.save(message);
        } catch(JsonProcessingException e){
            log.error("saveMessageOutbox : {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteToken(PaymentEvent.DeleteToken event) {
        log.info("handleDeleteToken : {} ", event);
        paymentProducer.send("payment-completed-event-v1",event);
    }
}
