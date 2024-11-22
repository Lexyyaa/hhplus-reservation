package com.hhplus.reservation.domain.message;

import com.hhplus.reservation.domain.payment.PaymentEvent;

import java.util.List;

public interface MessageOutboxRepository {

    MessageOutbox save(MessageOutbox messageOutbox);

    MessageOutbox findByEventIdAndEventType(Long messageKey, String eventType);

    List<MessageOutbox> findRetryMessages();
}
