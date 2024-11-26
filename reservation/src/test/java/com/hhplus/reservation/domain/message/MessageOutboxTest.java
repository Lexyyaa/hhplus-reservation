package com.hhplus.reservation.domain.message;

import com.hhplus.reservation.domain.payment.PaymentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class MessageOutboxTest {

    @Test
    @DisplayName("MessageOutbox 상태 전환 테스트")
    public void testPublishedMethod() {
        // Given
        MessageOutbox messageOutbox = MessageOutbox.builder()
                .eventId(123L)
                .eventType(PaymentEvent.EventType.DELETE_TOKEN.name())
                .domainName("Payment")
                .topic("payment-topic")
                .payload("{\"amount\":1000}")
                .eventStatus(MessageStatus.INIT)
                .retryCnt(0L)
                .build();

        // When
        messageOutbox.published();

        // Then
        assertThat(messageOutbox.getEventStatus()).isEqualTo(MessageStatus.PUBLISHED);
    }

    @Test
    @DisplayName("MessageOutbox 재시도 횟수 증가 테스트")
    public void testCountUpMethod() {
        // Given
        MessageOutbox messageOutbox = MessageOutbox.builder()
                .eventId(123L)
                .eventType(PaymentEvent.EventType.DELETE_TOKEN.name())
                .domainName("Payment")
                .topic("payment-topic")
                .payload("{\"amount\":1000}")
                .eventStatus(MessageStatus.INIT)
                .retryCnt(0L)
                .build();

        // When
        messageOutbox.countUp();

        // Then
        assertThat(messageOutbox.getRetryCnt()).isEqualTo(1L);
    }

}