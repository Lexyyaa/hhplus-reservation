package com.hhplus.reservation.domain.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class WaitingQueueIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("성공적으로 토큰을 생성한다.")
    void 성공적으로_토큰을_생성한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/queue/token/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("토큰 조회에 실패한다 - 토큰이 존재하지 않을 때")
    void 토큰_조회에_실패한다_토큰이_존재하지_않을_때() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/queue/polling/1")
                        .header("Authorization", "invalid_token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("토큰이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("토큰 검증에 실패한다 - 잘못된 토큰")
    void 토큰_검증에_실패한다_잘못된_토큰() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/queue/polling/1")
                        .header("Authorization", "invalid_token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }

    @Test
    @DisplayName("진행 가능한 최대 수를 초과하면 예외를 발생시킨다.")
    void 진행_가능한_최대_수를_초과하면_예외를_발생시킨다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/queue/process")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최대 진행 수를 초과하였습니다."));
    }
}
