package com.hhplus.reservation.infra.point;

import com.hhplus.reservation.domain.point.UserPoint;
import com.hhplus.reservation.domain.point.UserPointRepository;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorCode;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final JPAUserPointRepository jpaRepository;
    @Override
    public UserPoint findByUserId(Long userId) {
        return jpaRepository.findById(userId).orElseThrow(
                () -> new BizException(ErrorType.USER_NOT_FOUND));
    }

    @Override
    public UserPoint findByUserIdWithLock(Long userId) {
        return jpaRepository.findByUserIdWithLock(userId).orElseThrow(
                () -> new BizException(ErrorType.USER_NOT_FOUND));
    }

    @Override
    public UserPoint save(UserPoint userPoint) {
        return jpaRepository.save(userPoint);
    }
}
