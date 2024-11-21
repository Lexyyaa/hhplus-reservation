package com.hhplus.reservation.domain.message;

import java.util.List;

public interface MessageOutboxRepository {

    MessageOutbox save(MessageOutbox messageOutbox);

    MessageOutbox findByEventIdAndEventType(Long eventId, String eventType);

    List<MessageOutbox> findRetryMessages();
}
