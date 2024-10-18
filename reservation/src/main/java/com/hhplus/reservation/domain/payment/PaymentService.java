package com.hhplus.reservation.domain.payment;

import com.hhplus.reservation.application.dto.ReservationInfo;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void validatePayment(ReservationInfo reservation){
        Payment.isExpiredPayment(reservation.getReserveExpiredAt());
        Payment.isValidPrice(reservation.getTotalPrice());

        boolean isPaid = paymentRepository.existsByReservationId(reservation.getId());
        Payment.isPaidPayment(isPaid);
    }

    public Payment savePayment(Long userId,ReservationInfo reservation){
        Payment payment = Payment.builder()
                .userId(userId)
                .reservationId(reservation.getId())
                .price(reservation.getTotalPrice())
                .paidAt(LocalDateTime.now())
                .build();
        return paymentRepository.save(payment);
    }
}


