package com.hhplus.reservation.infra.point;

import com.hhplus.reservation.domain.point.UserPoint;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JPAUserPointRepository extends JpaRepository<UserPoint,Long> {
    Optional<UserPoint> findById(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserPoint u WHERE u.id = :userId")
    Optional<UserPoint> findByUserIdWithLock(@Param("userId") Long userId);
}
