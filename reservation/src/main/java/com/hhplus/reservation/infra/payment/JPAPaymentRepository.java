package com.hhplus.reservation.infra.payment;

import com.hhplus.reservation.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAPaymentRepository extends JpaRepository<Payment,Long> {
    boolean existsByReservationId(Long reservationId);
}
