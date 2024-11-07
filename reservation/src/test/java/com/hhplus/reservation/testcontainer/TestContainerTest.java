package com.hhplus.reservation.testcontainer;

import com.hhplus.reservation.config.TestContainersConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ContextConfiguration(classes = TestContainersConfig.class)
@ActiveProfiles("test")
public class TestContainerTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnection() {
        // Redis에 데이터 저장
        redisTemplate.opsForValue().set("testKey", "testValue");

        // Redis에서 데이터 조회
        String value = redisTemplate.opsForValue().get("testKey");

        log.info("value : {} ",value);

        // 값 확인
        assertEquals("testValue", value);
    }
}