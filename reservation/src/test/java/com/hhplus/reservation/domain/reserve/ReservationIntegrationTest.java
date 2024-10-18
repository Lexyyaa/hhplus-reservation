package com.hhplus.reservation.domain.reserve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ReservationIntegrationTest {

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
    @DisplayName("좌석 예약을 성공적으로 생성한다")
    void 좌석_예약을_성공적으로_생성한다() throws Exception {
        ReserveSeatRequest request = new ReserveSeatRequest(1L, List.of(1L, 2L));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reserve/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalPrice").exists());
    }

    @Test
    @DisplayName("예약 조회에 성공한다")
    void 예약_조회에_성공한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reserve/1")
                        .header("Authorization", "valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("예약 조회에 실패한다 - 예약이 존재하지 않을 때")
    void 예약_조회에_실패한다_예약이_존재하지_않을_때() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reserve/999")
                        .header("Authorization", "valid_token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("예약내역이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("좌석 예약에 실패한다 - 잘못된 토큰")
    void 좌석_예약에_실패한다_잘못된_토큰() throws Exception {
        ReserveSeatRequest request = new ReserveSeatRequest(1L, List.of(1L, 2L));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reserve/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "invalid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }
}