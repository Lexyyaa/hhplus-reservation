package com.hhplus.reservation.infra.message;

import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JPAMessageOutboxRepository extends JpaRepository<MessageOutbox, Long> {

    @Query("SELECT m FROM MessageOutbox m WHERE m.eventId = :eventId AND m.eventType = :eventType")
    Optional<MessageOutbox> findByEventIdAndEventType(@Param("eventId") Long eventId, @Param("eventType") String eventType);

    @Query("SELECT m FROM MessageOutbox m WHERE m.eventStatus = 'INIT' and m.retryCnt < 3 ")
    List<MessageOutbox> findRetryMessages();
}
