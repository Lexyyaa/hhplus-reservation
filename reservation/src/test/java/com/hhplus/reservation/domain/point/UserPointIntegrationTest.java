package com.hhplus.reservation.domain.point;

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
class UserPointIntegrationTest {

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
    @DisplayName("포인트 충전에 성공한다")
    void 포인트_충전에_성공한다() throws Exception {
        Long amount = 100L;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point/charge/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.point").exists());
    }

    @Test
    @DisplayName("포인트 조회에 성공한다")
    void 포인트_조회에_성공한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/point/check/1")
                        .header("Authorization", "valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.point").exists());
    }

    @Test
    @DisplayName("포인트 충전에 실패한다 - 잘못된 토큰")
    void 포인트_충전에_실패한다_잘못된_토큰() throws Exception {
        Long amount = 100L;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point/charge/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "invalid_token")
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }

    @Test
    @DisplayName("포인트 결제에 실패한다 - 포인트가 부족할 때")
    void 포인트_결제에_실패한다_포인트가_부족할_때() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/point/pay/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content("{\"price\": 200}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("포인트가 부족합니다."));
    }
}