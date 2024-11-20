package com.hhplus.reservation.domain.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageOutboxService {

    private final MessageOutboxRepository messageOutboxRepository;

    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxRepository.save(messageOutbox);
    }
}
