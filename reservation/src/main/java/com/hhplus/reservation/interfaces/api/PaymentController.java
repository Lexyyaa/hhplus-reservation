package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.application.usecase.PaymentUsecase;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentUsecase paymentUsecase;

    @Operation(summary = "결제 요청", description = "사용자가 예약한 좌석을 결제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 성공"),
            @ApiResponse(responseCode = "409", description = "이미 결제된 예약입니다."),
            @ApiResponse(responseCode = "400", description = "결제 시간이 초과되었습니다."),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 결제 토큰입니다.")
    })
    @PostMapping("/{reservationId}/{userId}")
    public PaymentResponse payment(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("reservationId") Long reservationId,
            @PathVariable("userId") Long userId,
            @RequestBody Long amount
    ) {
        return paymentUsecase.pay(queueToken,reservationId,userId,amount);
    }
}