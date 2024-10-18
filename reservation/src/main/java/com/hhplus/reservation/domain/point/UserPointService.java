package com.hhplus.reservation.domain.point;

import com.hhplus.reservation.application.dto.UserPointInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;

    public UserPointInfo chargePoint(Long userId, Long amount){

        UserPoint.isValidAmount(amount);

        UserPoint currUserPoint = userPointRepository.findByUserId(userId);
        currUserPoint.chargePoint(amount);

        UserPoint userPoint = userPointRepository.save(currUserPoint);

        return UserPoint.convert(userPoint);
    }

    public UserPointInfo checkPoint(Long userId){
        UserPoint currUserPoint = userPointRepository.findByUserId(userId);
        return UserPoint.convert(currUserPoint);
    }

    public UserPointInfo payPoint(Long userId,Long price){
        UserPoint currUserPoint = userPointRepository.findByUserIdWithLock(userId);
        UserPoint.isValidUser(currUserPoint,price);
        currUserPoint.payPoint(price);
        UserPoint userPoint = userPointRepository.save(currUserPoint);
        return UserPoint.convert(userPoint);
    }

}
