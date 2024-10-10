package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

@RestController
@RequestMapping("/api/pay")
public class PaymentController {

    // Mock 대기열 (유효한 토큰 저장)
    public static Queue<String> waitingQueue = new LinkedList<>();

    static {
        // 미리 대기열에 토큰 추가
        waitingQueue.add("MTA6ZGM0ZDU0NDYtNDg5NC00NzgzLWFmMmYtODZiYjUwNDgxOTdh");
    }

    // 결제 API
    @PostMapping("/{reservationId}")
    public ResponseEntity<PaymentResponse> payment(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("reservationId") Long reservationId) {

        validateToken(queueToken);

        PaymentResponse paymentResponse = payment(reservationId);
        return ResponseEntity.ok().body(paymentResponse);
    }

    // Mock API용 메소드
    private PaymentResponse payment(Long reservationId){
        return new PaymentResponse(
                1L,
                10L,
                "흠뻑쇼",
                LocalDate.parse("2025-08-08"),
                250000L,
                LocalDateTime.now());
    }

    private void validateToken(String queueToken) {
        if (queueToken == null || !waitingQueue.contains(queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }
    }
}
