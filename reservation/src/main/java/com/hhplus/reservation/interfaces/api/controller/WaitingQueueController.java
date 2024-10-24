package com.hhplus.reservation.interfaces.api.controller;

import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueuePollingResponse;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
@Tag(name = "WaitingQueue", description = "대기열 토큰 API")
public class WaitingQueueController {

    public final WaitingQueueService queueService;

    @Operation(summary = "토큰 발급", description = "사용자 진입시 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰발급 성공"),
            @ApiResponse(responseCode = "404", description = "토큰이 존재하지 않습니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.")
    })
    @PostMapping("/token/{userId}")
    public WaitingQueueResponse issueToken(@PathVariable("userId") Long userId) {
        return queueService.getOrCreateQueueToken(userId);
    }

    @Operation(summary = "폴링", description = "대기번호를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰조회 성공"),
            @ApiResponse(responseCode = "404", description = "토큰이 존재하지 않습니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.")
    })
    @GetMapping("/polling/{userId}")
    public WaitingQueuePollingResponse polling(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String queueToken) {
        return queueService.getQueueToken(userId,queueToken);
    }
}