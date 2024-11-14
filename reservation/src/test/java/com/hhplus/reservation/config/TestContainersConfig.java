package com.hhplus.reservation.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainersConfig {

    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer<?> REDIS_CONTAINER;

    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0.33");
    private static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        // Redis 컨테이너 설정
        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        REDIS_CONTAINER.start();

        // MySQL 컨테이너 설정
        MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
                .withDatabaseName("test_db")
                .withUsername("test_user")
                .withPassword("test_password")
                .withReuse(true);
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        // Redis 속성 등록
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());

        // MySQL 속성 등록
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }
}