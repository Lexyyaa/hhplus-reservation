package com.hhplus.reservation.infra.point;

import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final JPAUserPointRepository jpaRepository;
    @Override
    public UserPoint findByUserId(Long userId) {
        return jpaRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_CHARGE_AMOUNT));
    }

    @Override
    public UserPoint findByUserIdWithLock(Long userId) {
        return jpaRepository.findByUserIdWithLock(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserPoint save(UserPoint userPoint) {
        return jpaRepository.save(userPoint);
    }
}
