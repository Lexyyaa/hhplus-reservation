package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.application.usecase.ConcertUsecase;
import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
@Tag(name = "Concert", description = "콘서트 정보조회 API")
public class ConcertController {

    private final ConcertUsecase concertUsecase;
    @Operation(summary = "날짜 조회", description = "예약가능한 날짜를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "409", description = "이미 결제된 예약입니다."),
            @ApiResponse(responseCode = "400", description = "콘서트 정보가 존재하지 않습니다.")
    })
    @GetMapping("/avaliable/{concertId}")
    public List<ConcertScheduleResponse> getAvailableDate(
            @PathVariable Long concertId,
            @RequestHeader("Authorization") String queueToken) {
        return concertUsecase.getSchedules(concertId,queueToken);
    }

    @Operation(summary = "좌석 조회", description = "예약가능한 좌석을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "409", description = "이미 예약된 좌석입니다."),
            @ApiResponse(responseCode = "400", description = "정보가 존재하지 않습니다.")
    })
    @GetMapping("/avaliable/seat/{concertScheduleId}")
    public List<ConcertSeatResponse> getAvailableSeat(
            @PathVariable Long concertScheduleId,
            @RequestHeader("Authorization") String queueToken) {
        return concertUsecase.getSeats(concertScheduleId,queueToken);
    }
}
