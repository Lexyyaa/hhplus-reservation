package com.hhplus.reservation.domain.point;
import com.hhplus.reservation.application.dto.UserPointInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPoint extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "point")
    private Long point;

    public static void isValidUser(UserPoint userPoint, Long amount) {
        if (userPoint.getPoint() < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
        }
    }

    public static void isValidAmount(Long amount) {
        if (amount < 0) {
            throw new CustomException(ErrorCode.INVALID_CHARGE_AMOUNT);
        }
    }

    public void chargePoint(Long amount) {
        this.point += amount;
    }

    public void payPoint(Long amount) {
        this.point -= amount;
    }

    public static UserPointInfo convert(UserPoint userPoint) {
        return UserPointInfo.builder()
                .userId(userPoint.getId())
                .point(userPoint.getPoint())
                .build();
    }
}