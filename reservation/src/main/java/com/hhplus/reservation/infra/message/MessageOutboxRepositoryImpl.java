package com.hhplus.reservation.infra.message;

import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxRepository;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageOutboxRepositoryImpl implements MessageOutboxRepository {

    private final JPAMessageOutboxRepository messageOutboxRepository;

    @Override
    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxRepository.save(messageOutbox);
    }

    @Override
    public MessageOutbox findByEventIdAndEventType(Long eventId, String eventType) {
        return messageOutboxRepository.findByEventIdAndEventType(eventId,eventType).orElseThrow(
                () -> new BizException(ErrorType.OUTBOX_NOT_FOUND)
        );
    }

    @Override
    public List<MessageOutbox> findRetryMessages() {
        return messageOutboxRepository.findRetryMessages();
    }
}
