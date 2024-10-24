package com.hhplus.reservation.domain.point;

import com.hhplus.reservation.application.dto.UserPointInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;

    /**
     * 포인트를 충전한다
     */
    public UserPointInfo chargePoint(Long userId, Long amount){

        UserPoint.isValidAmount(amount);

        UserPoint currUserPoint = userPointRepository.findByUserIdWithLock(userId);
        currUserPoint.chargePoint(amount);

        UserPoint userPoint = userPointRepository.save(currUserPoint);

        return UserPoint.convert(userPoint);
    }

    /**
     * 포인트를 조회한다.
     */
    public UserPointInfo checkPoint(Long userId){
        UserPoint currUserPoint = userPointRepository.findByUserId(userId);
        return UserPoint.convert(currUserPoint);
    }

    /**
     * 포인트를 사용한다.
     */
    public UserPointInfo payPoint(Long userId,Long price){
        UserPoint currUserPoint = userPointRepository.findByUserIdWithLock(userId);
        UserPoint.isEnoughPoint(currUserPoint,price);
        currUserPoint.payPoint(price);

        UserPoint userPoint = userPointRepository.save(currUserPoint);

        return UserPoint.convert(userPoint);
    }
}
