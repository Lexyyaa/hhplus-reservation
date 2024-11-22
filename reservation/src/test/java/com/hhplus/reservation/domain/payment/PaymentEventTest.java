package com.hhplus.reservation.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentEventTest {

    @Test
    @DisplayName("페이로드_이벤트로_변환")
    public void 페이로드_이벤트로_변환() throws JsonProcessingException {
        // Given
        String token = "sampleToken";
        Long messageKey = 123L;
        String payload = String.format(
                "{\"token\":\"%s\", \"messageKey\":%d, \"eventType\":\"DELETE_TOKEN\"}",
                token, messageKey
        );

        // When
        PaymentEvent.DeleteToken deleteToken = PaymentEvent.DeleteToken.convertToEvent(payload);

        // Then
        assertThat(deleteToken).isNotNull();
        assertThat(deleteToken.getToken()).isEqualTo(token);
        assertThat(deleteToken.getMessageKey()).isEqualTo(messageKey);
        assertThat(deleteToken.getEventType()).isEqualTo(PaymentEvent.EventType.DELETE_TOKEN);
    }
}
