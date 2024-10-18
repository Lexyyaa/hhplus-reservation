package com.hhplus.reservation.domain.payment;

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
class PaymentIntegrationTest {

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
    @DisplayName("결제 요청을 성공적으로 처리한다")
    void 결제_요청을_성공적으로_처리한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pay/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content(objectMapper.writeValueAsString(100L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    @DisplayName("결제 실패 - 만료된 예약")
    void 결제_실패_만료된_예약() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pay/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content(objectMapper.writeValueAsString(100L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("결제 시간이 초과되었습니다."));
    }

    @Test
    @DisplayName("결제 실패 - 이미 결제된 예약")
    void 결제_실패_이미_결제된_예약() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pay/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "valid_token")
                        .content(objectMapper.writeValueAsString(100L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("결제된 예약입니다."));
    }

    @Test
    @DisplayName("결제 실패 - 유효하지 않은 토큰")
    void 결제_실패_유효하지_않은_토큰() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/pay/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "invalid_token")
                        .content(objectMapper.writeValueAsString(100L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }
}
