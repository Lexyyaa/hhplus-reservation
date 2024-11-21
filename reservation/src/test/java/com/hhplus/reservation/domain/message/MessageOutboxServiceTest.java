package com.hhplus.reservation.domain.message;

import com.hhplus.reservation.domain.payment.PaymentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessageOutboxServiceTest {

    @Mock
    private MessageOutboxRepository messageOutboxRepository;

    @InjectMocks
    private MessageOutboxService messageOutboxService;

    @Test
    @DisplayName("아웃박스_메시지_저장")
    public void 아웃박스_메시지_저장() {
        // Given
        MessageOutbox messageOutbox = MessageOutbox.builder()
                .eventId(123L)
                .eventType(PaymentEvent.EventType.DELETE_TOKEN)
                .domainName("Payment")
                .topic("payment-topic")
                .payload("{\"amount\":1000}")
                .eventStatus(MessageStatus.INIT)
                .retryCnt(0L)
                .build();

        Mockito.when(messageOutboxRepository.save(Mockito.any(MessageOutbox.class))).thenReturn(messageOutbox);

        // When
        MessageOutbox savedMessageOutbox = messageOutboxService.save(messageOutbox);

        // Then
        Mockito.verify(messageOutboxRepository, Mockito.times(1)).save(Mockito.any(MessageOutbox.class));
        assertThat(savedMessageOutbox).isNotNull();
        assertThat(savedMessageOutbox.getEventId()).isEqualTo(123L);
    }

    @Test
    @DisplayName("메시지_발행_상태_변경")
    public void 메시지_발행_상태_변경() {
        // Given
        Long messageKey = 123L;
        String eventType = PaymentEvent.EventType.DELETE_TOKEN.name();

        MessageOutbox messageOutbox = MessageOutbox.builder()
                .eventId(messageKey)
                .eventType(PaymentEvent.EventType.DELETE_TOKEN)
                .eventStatus(MessageStatus.INIT)
                .build();

        Mockito.when(messageOutboxRepository.findByEventIdAndEventType(messageKey, eventType)).thenReturn(messageOutbox);

        // When
        messageOutboxService.chkPublished(messageKey, eventType);

        // Then
        assertThat(messageOutbox.getEventStatus()).isEqualTo(MessageStatus.PUBLISHED);
        assertThat(messageOutbox.getPublishedAt()).isNotNull();
    }

    @Test
    @DisplayName("재발행_메시지_목록_가져오기")
    public void 재발행_메시지_목록_가져오기() {
        // Given
        List<MessageOutbox> retryMessages = List.of(
                MessageOutbox.builder().eventId(1L).retryCnt(3L).eventStatus(MessageStatus.INIT).build(),
                MessageOutbox.builder().eventId(2L).retryCnt(2L).eventStatus(MessageStatus.INIT).build()
        );

        Mockito.when(messageOutboxRepository.findRetryMessages()).thenReturn(retryMessages);

        // When
        List<MessageOutbox> result = messageOutboxService.findRetryMessages();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRetryCnt()).isEqualTo(3L);
    }

    @Test
    @DisplayName("재시도_횟수_증가")
    public void 재시도_횟수_증가() {
        // Given
        MessageOutbox messageOutbox = MessageOutbox.builder()
                .eventId(123L)
                .retryCnt(0L)
                .eventStatus(MessageStatus.INIT)
                .build();

        Mockito.when(messageOutboxRepository.save(Mockito.any(MessageOutbox.class))).thenReturn(messageOutbox);

        // When
        MessageOutbox updatedMessageOutbox = messageOutboxService.countUpRetry(messageOutbox);

        // Then
        assertThat(updatedMessageOutbox.getRetryCnt()).isEqualTo(1L);
    }
}