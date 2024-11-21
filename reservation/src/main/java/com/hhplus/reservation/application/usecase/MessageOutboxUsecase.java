package com.hhplus.reservation.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxService;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.payment.PaymentEventPublisher;
import com.hhplus.reservation.domain.payment.PaymentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageOutboxUsecase {

    private MessageOutboxService messageOutboxService;
    private PaymentProducer paymentProducer;

    /**
     * 재시도 횟수가 3번 미만인 케이스만 재발행한다.
     */
    public void retryDeleteToken(){
        List<MessageOutbox> messageOutboxes = messageOutboxService.findRetryMessages();
        for(MessageOutbox messageOutbox : messageOutboxes){
            paymentProducer.send("payment-completed-event-v1",PaymentEvent.DeleteToken.convertToEvent(messageOutbox.getPayload()));
            messageOutboxService.countUpRetry(messageOutbox);
        }
    }
}
