package com.hhplus.reservation.domain.payment;

public interface PaymentRepository {
    boolean existsByReservationId(Long reservationId);
    Payment save(Payment payment);
}
