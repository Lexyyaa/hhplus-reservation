package com.hhplus.reservation.domain.point;

import com.hhplus.reservation.application.dto.UserPointInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;
//    private final RedissonClient redissonClient;

    /**
     * 포인트를 충전한다
     */
    @Transactional
    public UserPointInfo chargePoint(Long userId, Long amount) {
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
