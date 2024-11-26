package com.hhplus.reservation.infra.message;

import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxRepository;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageOutboxRepositoryImpl implements MessageOutboxRepository {

    private final JPAMessageOutboxRepository messageOutboxRepository;

    @Override
    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxRepository.save(messageOutbox);
    }

    @Override
    public MessageOutbox findByEventIdAndEventType(Long messageKey, String eventType) {
        return messageOutboxRepository.findByEventIdAndEventType(messageKey,eventType).orElseThrow(
                () -> new BizException(ErrorType.OUTBOX_NOT_FOUND)
        );
    }

    @Override
    public List<MessageOutbox> findRetryMessages() {
        return messageOutboxRepository.findRetryMessages();
    }
}
