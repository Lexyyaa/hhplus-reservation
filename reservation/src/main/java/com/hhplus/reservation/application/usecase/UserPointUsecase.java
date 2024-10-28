package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.UserPointInfo;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.interfaces.dto.point.UserPointResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointUsecase {

    private final UserPointService userPointService;
    @Transactional
    public UserPointResponse chargePoint(Long userId, Long amount){
        UserPointInfo userPointInfo = userPointService.chargePoint(userId, amount);
        return UserPointInfo.convert(userPointInfo);
    }

    public UserPointResponse checkPoint(Long userId){
        UserPointInfo userPointInfo = userPointService.checkPoint(userId);
        return UserPointInfo.convert(userPointInfo);
    }

}
