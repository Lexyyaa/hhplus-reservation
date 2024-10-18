package com.hhplus.reservation.domain.point;

public interface UserPointRepository {
    UserPoint findByUserId(Long userId);
    UserPoint findByUserIdWithLock(Long userId);
    UserPoint save(UserPoint userPoint);

}


