package com.hhplus.reservation.domain.payment;

import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public static void isExpiredPayment(LocalDateTime reserveExpiredAt){
        if (reserveExpiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.PAYMENT_EXPIRED);
        }
    }

    public static void isValidPrice(Long totalPrice){
        if(totalPrice < 0){
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }

    public static void isPaidPayment(boolean isPaid){
        if (isPaid) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_MADE);
        }
    }
}