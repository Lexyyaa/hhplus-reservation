package com.hhplus.reservation.testcontainer;

import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class KafkaTestContainer {

    private static final KafkaContainer KAFKA_CONTAINER;

    static {
        KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
        KAFKA_CONTAINER.start();
        
        System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
    }

    @AfterAll
    public static void stopContainer() {
        KAFKA_CONTAINER.stop();
    }
}
