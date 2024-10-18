package com.hhplus.reservation.infra.payment;

import com.hhplus.reservation.domain.payment.Payment;
import com.hhplus.reservation.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JPAPaymentRepository jpaPaymentRepository;
    @Override
    public boolean existsByReservationId(Long reservationId) {
        return jpaPaymentRepository.existsByReservationId(reservationId);
    }

    @Override
    public Payment save(Payment payment) {
        return jpaPaymentRepository.save(payment);
    }
}
