package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.application.usecase.ReservationUsecase;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "예약 API")
public class ReserveController {

    private final ReservationUsecase reservationUsecase;

    @Operation(summary = "좌석 예약", description = "사용자가 좌석을 예약합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 성공"),
            @ApiResponse(responseCode = "404", description = "예약 내역이 존재하지 않습니다."),
            @ApiResponse(responseCode = "400", description = "예약이 만료되었습니다."),
            @ApiResponse(responseCode = "409", description = "다른 사용자가 예약 중입니다.")
    })
    @PostMapping("{concertScheduleId}/seat/{userId}")
    public ReserveResponse reserveSeat(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @PathVariable("userId") Long userId,
            @RequestBody ReserveSeatRequest seatsRequest
    ) {
        return reservationUsecase.reserve(concertScheduleId,userId,queueToken,seatsRequest);
    }
}










