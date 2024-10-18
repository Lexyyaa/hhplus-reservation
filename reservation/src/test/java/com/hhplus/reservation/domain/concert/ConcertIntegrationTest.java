package com.hhplus.reservation.domain.concert;

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
public class ConcertIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("콘서트 정보를 성공적으로 조회한다.")
    void 콘서트_정보를_성공적으로_조회한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/available/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertId").value(1))
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("예약 가능한 날짜를 성공적으로 조회한다.")
    void 예약_가능한_날짜를_성공적으로_조회한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/available/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].performDate").exists());
    }

    @Test
    @DisplayName("콘서트 조회에 실패한다 - 콘서트 정보가 존재하지 않을 때")
    void 콘서트_조회에_실패한다_콘서트_정보가_존재하지_않을_때() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/available/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("콘서트 정보가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("예약 가능한 날짜 조회에 실패한다 - 스케줄이 존재하지 않을 때")
    void 예약_가능한_날짜_조회에_실패한다_스케줄이_존재하지_않을_때() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/available/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("예약 가능한 날짜가 존재하지 않습니다."));
    }
}
