package com.hhplus.reservation.domain.message;

public interface MessageOutboxRepository {
    MessageOutbox save(MessageOutbox messageOutbox);
}
