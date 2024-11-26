package com.hhplus.reservation.kafka;

import com.hhplus.reservation.support.config.KafkaConfig;
import com.hhplus.reservation.testcontainer.KafkaTestContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaIntegrationTest extends KafkaTestContainer {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private KafkaTestConsumer consumer;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeAll
    public void setup() {
        log.info("Kafka Integration Test 초기화 중...");
        consumer.clearEvents();
        log.info("Consumer 초기화 완료");
    }

    @BeforeEach
    public void beforeEach() {
        log.info("테스트 실행 전 Consumer 초기화");
        consumer.clearEvents();
    }

    @Test
    @DisplayName("Kafka_메시지_발행_및_소비")
    public void Kafka_메시지_발행_및_소비() {
        KafkaMessage message = new KafkaMessage("1", "Test JSON Message");
        log.info("테스트 메시지 생성: {}", message);

        producer.sendEvent(null, message);
        log.info("프로듀서로 메시지 발행 완료: {}", message);

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .until(() -> !consumer.getReceivedEvents().isEmpty());
        log.info("Consumer에서 메시지 수신 대기 완료");

        List<KafkaMessage> receivedEvents = consumer.getReceivedEvents();
        log.info("수신된 메시지: {}", receivedEvents);

        Assertions.assertEquals(1, receivedEvents.size());
        Assertions.assertEquals(message.getId(), receivedEvents.get(0).getId());
        Assertions.assertEquals(message.getMessage(), receivedEvents.get(0).getMessage());
    }

    @Test
    @DisplayName("동일_키의_파티션_유지_테스트")
    public void 동일_키의_파티션_유지_테스트() {
        String key = "user-123";
        List<KafkaMessage> eventsToSend = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> new KafkaMessage(key, "Message " + i))
                .toList();
        log.info("테스트 메시지 생성 완료: {}", eventsToSend);

        eventsToSend.forEach(event -> {
            producer.sendEvent(key, event);
            log.info("프로듀서로 메시지 발행: key={}, message={}", key, event);
        });

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .until(() -> consumer.getReceivedEvents().size() == eventsToSend.size());
        log.info("Consumer에서 모든 메시지 수신 완료");

        List<KafkaMessage> receivedEvents = consumer.getReceivedEvents();
        log.info("수신된 메시지: {}", receivedEvents);

        Assertions.assertEquals(eventsToSend.size(), receivedEvents.size());
        receivedEvents.forEach(event -> Assertions.assertEquals(key, event.getId()));
    }

    @Test
    @DisplayName("라운드로빈_분산_처리_테스트")
    public void 라운드로빈_분산_처리_테스트() {
        List<KafkaMessage> eventsToSend = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> new KafkaMessage(null, "Message " + i))
                .toList();
        log.info("테스트 메시지 생성 완료: {}", eventsToSend);

        eventsToSend.forEach(event -> {
            producer.sendEvent(null, event);
            log.info("프로듀서로 메시지 발행: message={}", event);
        });

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .until(() -> consumer.getReceivedEvents().size() == eventsToSend.size());
        log.info("Consumer에서 모든 메시지 수신 완료");

        List<KafkaMessage> receivedEvents = consumer.getReceivedEvents();
        log.info("수신된 메시지: {}", receivedEvents);

        Map<Integer, Long> partitionDistribution = receivedEvents.stream()
                .collect(Collectors.groupingBy(
                        event -> Math.abs(event.hashCode()) % 3,
                        Collectors.counting()
                ));
        log.info("파티션 분배 결과: {}", partitionDistribution);

        Assertions.assertTrue(partitionDistribution.values().stream().allMatch(count -> count > 0));
    }
}

