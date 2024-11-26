package com.hhplus.reservation.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxRepository;
import com.hhplus.reservation.domain.message.MessageStatus;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import com.hhplus.reservation.domain.payment.PaymentRepository;
import com.hhplus.reservation.domain.queue.WaitingQueueRepository;
import com.hhplus.reservation.infra.payment.JPAPaymentRepository;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.testcontainer.KafkaTestContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
public class PaymentUsecaseKafkaTest extends KafkaTestContainer {

    @Autowired
    private PaymentUsecase paymentUsecase;

    @Autowired
    private JPAPaymentRepository paymentRepository;

    @Autowired
    private MessageOutboxRepository messageOutboxRepository;

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @BeforeEach
    void setup() {
        messageQueue.clear();
        paymentRepository.deleteAll();
    }

    @KafkaListener(topics = "payment-completed-event-v1", groupId = "test-group")
    public void collectMessages(String message) {
        messageQueue.offer(message);
        log.info("KafkaListener received: {}", message);
    }

    @Test
    @DisplayName("Kafka_발행_결제_성공")
    public void Kafka_발행_결제_성공() throws Exception {

        // Given: 이벤트 생성
        String token = "MV9fYzRjYTQyMzgtYTBiOS0zMzgyLThkY2MtNTA5YTZmNzU4NDli";
        Long reservationId = 1L;
        Long userId = 1L;

        PaymentResponse pay = paymentUsecase.pay(token, reservationId, userId);

        MessageOutbox outbox = messageOutboxRepository.findByEventIdAndEventType(
                pay.getId(),
                PaymentEvent.EventType.DELETE_TOKEN.name()
        );
        assertNotNull(outbox);
        assertEquals(MessageStatus.INIT, outbox.getEventStatus());
        log.info("Outbox entry: {}", outbox);

        // 메시지 수신 대기
        String receivedMessage = await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> messageQueue.poll(), msg -> msg != null);

        assertNotNull(receivedMessage);
        log.info("Received message: {}", receivedMessage);

        // 메시지 역직렬화
        PaymentEvent.DeleteToken receivedEvent = objectMapper.readValue(receivedMessage, PaymentEvent.DeleteToken.class);
        assertEquals(token, receivedEvent.getToken());
        log.info("receivedEvent: {}", receivedEvent);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    MessageOutbox updatedOutbox = messageOutboxRepository.findByEventIdAndEventType(pay.getId(), PaymentEvent.EventType.DELETE_TOKEN.name());
                    return updatedOutbox.getEventStatus() == MessageStatus.PUBLISHED;
                });

        MessageOutbox updatedOutbox = messageOutboxRepository.findByEventIdAndEventType(pay.getId(), PaymentEvent.EventType.DELETE_TOKEN.name());
        assertEquals(MessageStatus.PUBLISHED, updatedOutbox.getEventStatus());
        assertNotNull(updatedOutbox.getPublishedAt());

        // 토큰 삭제 확인
        assertNull(waitingQueueRepository.findWaitingQueueByToken(token));
    }

    @Test
    @DisplayName("Kafka_발행_결제_실패")
    public void Kafka_발행_결제_실패() {
        String invalidToken = "INVALID_TOKEN";
        Long reservationId = 999L; // 존재하지 않는 예약 ID
        Long userId = 1L;

        // 예외 발생 검증
        assertThrows(BizException.class, () -> {
            paymentUsecase.pay(invalidToken, reservationId, userId);
        });

        assertTrue(messageQueue.isEmpty());
    }
}

