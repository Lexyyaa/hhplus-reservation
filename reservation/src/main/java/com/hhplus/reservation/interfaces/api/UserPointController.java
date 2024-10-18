package com.hhplus.reservation.interfaces.api;
import com.hhplus.reservation.application.usecase.UserPointUsecase;
import com.hhplus.reservation.interfaces.dto.point.UserPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
@Tag(name = "UserPoint", description = "사용자포인트 API")
public class UserPointController {

    private final UserPointUsecase userPointUsecase;

    @Operation(summary = "포인트 충전", description = "사용자가 요청한 포인트를 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "충전 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "400", description = "충전 포인트는 0보다 커야 합니다.")
    })
    @PostMapping("/charge/{userId}")
    public UserPointResponse chargePoint(@RequestHeader("Authorization") String queueToken,
                                         @PathVariable("userId") Long userId,
                                         @RequestBody Long amount) {
        return userPointUsecase.chargePoint(userId,amount,queueToken);
    }

    @Operation(summary = "포인트 조회", description = "사용자의포인트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    // 잔액 조회 API
    @GetMapping("/check/{userId}")
    public UserPointResponse checkPoint(@RequestHeader("Authorization") String queueToken,
                                                        @PathVariable("userId") Long userId) {
        return userPointUsecase.checkPoint(userId,queueToken);
    }
}
