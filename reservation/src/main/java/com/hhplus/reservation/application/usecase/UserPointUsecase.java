package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.UserPointInfo;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.interfaces.dto.point.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointUsecase {

    private final UserPointService userPointService;

    /**
     * 포인트를 충전한다.
     */
    public UserPointResponse chargePoint(Long userId, Long amount){
        UserPointInfo userPointInfo = userPointService.chargePoint(userId, amount);
        return UserPointInfo.convert(userPointInfo);
    }

    /**
     * 보유 포인트를 조회한다.
     */
    public UserPointResponse checkPoint(Long userId){
        UserPointInfo userPointInfo = userPointService.checkPoint(userId);
        return UserPointInfo.convert(userPointInfo);
    }

}
