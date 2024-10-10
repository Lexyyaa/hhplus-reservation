package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.interfaces.dto.point.PointChargeRequest;
import com.hhplus.reservation.interfaces.dto.point.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Queue;

@RestController
@RequestMapping("/api/point")
public class PointController {

    // Mock 대기열 (유효한 토큰 저장)
    public static Queue<String> waitingQueue = new LinkedList<>();

    static {
        // 미리 대기열에 토큰 추가
        waitingQueue.add("MTA6ZGM0ZDU0NDYtNDg5NC00NzgzLWFmMmYtODZiYjUwNDgxOTdh");
    }

    // 잔액 충전 API
    @PostMapping("/charge/{userId}")
    public ResponseEntity<PointResponse> chargePoint(@RequestHeader("Authorization") String queueToken,
                                      @PathVariable("userId") Long userId, @RequestBody PointChargeRequest request) {
        validateToken(queueToken);
        return ResponseEntity.ok().body(new PointResponse(10L, 10000));
    }

    // 잔액 조회 API
    @GetMapping("/check/{userId}")
    public ResponseEntity<PointResponse> checkPoint(@RequestHeader("Authorization") String queueToken,@PathVariable("userId") Long userId) {
        validateToken(queueToken);
        return ResponseEntity.ok().body(new PointResponse(10L, 10000));
    }

    private void validateToken(String queueToken) {
        if (queueToken == null || !waitingQueue.contains(queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }
    }
}